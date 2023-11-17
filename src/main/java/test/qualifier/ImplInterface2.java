package test.qualifier;

import framework.annotations.Qualifier;

@Qualifier("interface2")
public class ImplInterface2 implements Interface2 {

    private String name = "Qualifier - interface1";

    public ImplInterface2() {

    }
}
