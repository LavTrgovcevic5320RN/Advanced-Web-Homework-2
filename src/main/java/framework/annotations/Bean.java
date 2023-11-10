package framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Bean {//Vrednosti scope-a su singleton (default) i prototype
    String scope() default "singleton";
}
