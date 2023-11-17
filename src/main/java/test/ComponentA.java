package test;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean(scope = "prototype")
@Qualifier("ComponentA")
public class ComponentA implements Component {

    @Autowired(verbose = true)
    private ComponentC componentC;

    public ComponentA() {
    }
}
