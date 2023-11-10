package server;


import framework.annotations.Controller;
import framework.annotations.GET;
import framework.annotations.Path;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DiscoveryMechanism {

    private static DiscoveryMechanism instance;
    private ArrayList<HttpRoute> mapOfControllerHttpRoutes;

    private DiscoveryMechanism(){
    }

    public static DiscoveryMechanism getInstance(){
        if(instance == null){
            instance = new DiscoveryMechanism();
            instance.mapOfControllerHttpRoutes = new ArrayList<>();
            instance.mapRoutes();
        }
        return instance;
    }

    public Set<Class> findAllClasses(String packageName) {//pronalazi sve klase u paketu
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    public void mapRoutes(){
        Set<Class> classes = instance.findAllClasses("test");

        for (Class cl : classes) {
            if (cl.isAnnotationPresent(Controller.class)) {//nadjemo kontrolere
                Method[] classMethods = cl.getMethods();

                for(int i = 0; i < classMethods.length; i++){//prodjemo kroz sve metode
                    String httpMethod = "";

                    if(classMethods[i].isAnnotationPresent(Path.class)){//ako je http metoda napravimo novu rutu
                        Path p = classMethods[i].getAnnotation(Path.class);

                        if(classMethods[i].isAnnotationPresent(GET.class)) httpMethod = "GET";
                        else httpMethod = "POST";

                        HttpRoute httpRoute = new HttpRoute(p.path(), httpMethod, cl, classMethods[i].toString());
                        instance.mapOfControllerHttpRoutes.add(httpRoute);
                    }
                }
            }
        }
        for (HttpRoute r : mapOfControllerHttpRoutes) System.out.println(r);
    }

    public ArrayList<HttpRoute> getMapOfControllerRoutes() {
        return mapOfControllerHttpRoutes;
    }
}
