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
            instance.dependencyContainer.mapQualifiers();
        }
        return instance;
    }


    public Object returnClassInstance(Class cl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(singletonsMap.containsKey(cl.getName())) {
            return singletonsMap.get(cl.getName());
        }
        else {
            Constructor constructor = cl.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            singletonsMap.put(obj.getClass().getName(), obj);
            return obj;
        }
    }
    
    public void initDependencies(String controllerName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class cl = Class.forName(controllerName);
        Object controllerObj = instance.returnClassInstance(cl);
        instance.singletonsMap.put(controllerObj.getClass().getName(), controllerObj);
        Field[] controllerFields = cl.getDeclaredFields();
        controllerInitialisation(controllerObj, controllerFields);
    }

    public void controllerInitialisation(Object parentObj, Field[] fields) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        System.out.println("--Recursion--");

        for(Field field : fields){
            System.out.println("Field: " + field);

            if(field.isAnnotationPresent(Autowired.class)){
                Object obj = null;
                Class cl = null;
                Constructor constructor;

                if(field.getType().isInterface()){
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);

                    if(qualifier != null){
                        cl = instance.dependencyContainer.returnImplementation(qualifier.value());
                    }
                    else try {
                        throw new MissingQualifierException("Qualifier annotation missing from interface");
                    }
                    catch (MissingQualifierException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    String[] str = field.toString().split(" ");
                    cl = Class.forName(str[1]);
                }
                constructor = Objects.requireNonNull(cl).getDeclaredConstructor();

                if(cl.isAnnotationPresent(Bean.class)){
                    Bean bean = (Bean) cl.getAnnotation(Bean.class);
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

                Autowired autowired = field.getAnnotation(Autowired.class);
                if(autowired.verbose()){
                    System.out.println("[Initialized " + field.getType() + " " + field.getName() + " in " + parentObj.getClass().getName() +
                            " on " + LocalDateTime.now() + " with hash code " + Objects.requireNonNull(obj).hashCode() + "]" + " verbose " );
                }

                controllerInitialisation(obj, cl.getDeclaredFields());
            }
        }
    }



}
