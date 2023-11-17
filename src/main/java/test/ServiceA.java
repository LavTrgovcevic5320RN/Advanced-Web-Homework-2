package test;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean()
@Qualifier("ServiceA")
public class ServiceA implements Service {

    @Autowired(verbose = true)
    private ComponentA componentA;

    public ServiceA() {
    }
}
