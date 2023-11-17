package test.qualifier;

import framework.annotations.Qualifier;

@Qualifier("interface1")
public class ImplInterface1 implements Interface1 {

    private String name = "Qualifier - interface1";

    public ImplInterface1() {

    }
}
