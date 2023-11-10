package framework.engine;

import framework.annotations.*;
import framework.exceptions.AutowiredException;
import framework.exceptions.MissingQualifierException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DIEngine {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";


    private static DIEngine instance;
    DependencyContainer dependencyContainer;
    HashMap<String, Object> singletonsMap;

    private DIEngine(){
    }

    public static DIEngine getInstance(){
        if(instance == null){
            instance = new DIEngine();
            instance.singletonsMap = new HashMap<>();
            instance.dependencyContainer = new DependencyContainer();
            instance.dependencyContainer.mapQualifiers();//cim instanciramo DIengine mapiramo sve qualifiere u e
        }
        return instance;
    }


    public Object returnClassInstance(Class cl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(singletonsMap.containsKey(cl.getName())) {//ako ima singletona u mapi vrati
            return singletonsMap.get(cl.getName());
        }
        else {
            Constructor constructor = cl.getDeclaredConstructor();//ako nema napravi novi, stavi u mapu i vrati
            Object obj = constructor.newInstance();
            singletonsMap.put(obj.getClass().getName(), obj);
            return obj;
        }
    }

    //poziva se iz serverThread
    public void initDependencies(String controllerName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class cl = Class.forName(controllerName);//nadjemo kontroler
        Object controllerObj = instance.returnClassInstance(cl);
        instance.singletonsMap.put(controllerObj.getClass().getName(), controllerObj);//instanciramo kontroler
        Field[] controllerFields = cl.getDeclaredFields();
        controllerInitialisation(controllerObj, controllerFields);//inicijalizujemo sve servise
    }

    public void controllerInitialisation(Object parentObj, Field[] fields) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        System.out.println(ANSI_PURPLE + "--Recursion--" + ANSI_RESET);

//        System.out.println("-------------mapa--------------");
//        for (HashMap.Entry<String,Object> entry : singletonsMap.entrySet())
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        for(Field field : fields){
            System.out.println(ANSI_YELLOW + "Field: " + ANSI_RESET + ANSI_BLUE + field + ANSI_RESET);

            if(field.isAnnotationPresent(Autowired.class)){
                Object obj = null;
                Class cl = null;
                Constructor constructor;

                if(field.getType().isInterface()){//ako je interface gledamo da li ima qualifier, i ako nema error
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);

                    if(qualifier != null){
                        cl = instance.dependencyContainer.returnImplementation(qualifier.value());//posto mora biti singleton vracamo instancu id dep containera
                    }
                    else try {
                        throw new MissingQualifierException("Qualifier annotation missing from interface");
                    }
                    catch (MissingQualifierException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    String[] str = field.toString().split(" ");//ako nije interface uzmemo ime polja
                    cl = Class.forName(str[1]);
                }
                constructor = Objects.requireNonNull(cl).getDeclaredConstructor();

                if(cl.isAnnotationPresent(Bean.class)){
                    Bean bean = (Bean) cl.getAnnotation(Bean.class); //proverimo da li je bean, service ili component i u zavisnosti pravimo singleton ili ne
                    if(bean.scope().equals("singleton"))
                        obj = instance.returnClassInstance(cl);
                    else if(bean.scope().equals("prototype"))
                        obj = constructor.newInstance();
                }

                else if (cl.isAnnotationPresent(Service.class))
                    obj = instance.returnClassInstance(cl);

                else if (cl.isAnnotationPresent(Component.class))
                    obj = constructor.newInstance();

                else try {
                        throw new AutowiredException("Missing other annotations next to autowired");
                    }
                    catch (AutowiredException e) {
                        e.printStackTrace();
                    }
                field.setAccessible(true);
                field.set(parentObj, obj);

                Autowired autowired = field.getAnnotation(Autowired.class);//ako je verbose ispisi dodatne informacije
                if(autowired.verbose()){
                    System.out.println("[Initialized " + field.getType() + " " + field.getName() + " in " + parentObj.getClass().getName() +
                            " on " + LocalDateTime.now() + " with hash code " + Objects.requireNonNull(obj).hashCode() + "]" + ANSI_RED + " verbose " + ANSI_RESET );
                }

                controllerInitialisation(obj, cl.getDeclaredFields());//udji rekurzivno u svaki objekat
            }
        }
    }



}
