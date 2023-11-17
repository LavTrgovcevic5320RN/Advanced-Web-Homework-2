package test;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;

@framework.annotations.Component
@Qualifier("ComponentB")
public class ComponentB implements Component {

    @Autowired(verbose = true)
    @Qualifier("ComponentC")
    private Component componentC;

    public ComponentB() {
    }
}
