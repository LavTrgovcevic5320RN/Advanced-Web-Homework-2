package framework.engine;

import framework.annotations.Qualifier;
import framework.exceptions.NonExistingQualifierException;
import framework.exceptions.QualifierMustBeUniqueException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DependencyContainer {
    HashMap<String, Class> specToImplMap;

    public DependencyContainer() {
        this.specToImplMap = new HashMap<>();
    }

    public void registerImplementation(String qualifier, Class cl){
        this.specToImplMap.put(qualifier, cl);
    }

    public void mapQualifiers(){
        Set<Class> classes = this.getClassesInPackage("test");

        for (Class cl : classes) {
            if(cl.getAnnotation(Qualifier.class)!=null) {
                Qualifier qualifier = (Qualifier) cl.getAnnotation(Qualifier.class);

                if(this.specToImplMap.containsKey(qualifier.value())){
                    try {
                        throw new QualifierMustBeUniqueException("Qualifier must be unique");
                    } catch (QualifierMustBeUniqueException e) {
                        e.printStackTrace();
                    }
                } else
                    this.registerImplementation(qualifier.value(), cl);
            }
        }
    }

    public Set<Class> getClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    public Class returnImplementation(String qualifier){
        if(this.specToImplMap.containsKey(qualifier))
            return specToImplMap.get(qualifier);
        else try {
            throw new NonExistingQualifierException("Qualifier doesn't exist");
        } catch (NonExistingQualifierException e) {
            e.printStackTrace();
            return null;
        }
    }
}
