package test;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;

@framework.annotations.Component
@Qualifier("ComponentC")
public class ComponentC implements Component {

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private Service serviceC;

    public ComponentC() {
    }
}
