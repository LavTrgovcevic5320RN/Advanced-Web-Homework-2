package test;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;

@framework.annotations.Service
@Qualifier("ServiceC")
public class ServiceC implements Service {

    @Autowired(verbose = true)
    @Qualifier("ServiceD")
    private Service serviceD;

    public ServiceC() {
    }
}
