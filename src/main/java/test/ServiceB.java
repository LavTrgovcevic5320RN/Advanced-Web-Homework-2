package test;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;

@framework.annotations.Service
@Qualifier("ServiceB")
public class ServiceB implements Service {

    @Autowired(verbose = true)
    private ComponentB componentB;

    public ServiceB() {
    }
}
