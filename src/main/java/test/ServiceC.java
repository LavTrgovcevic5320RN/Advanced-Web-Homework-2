package test;

import framework.annotations.Autowired;
import framework.annotations.Qualifier;
import framework.annotations.Service;

@Service
@Qualifier("ServiceC")
public class ServiceC implements ServiceInter {

    @Autowired(verbose = true)
    @Qualifier("ServiceD")
    private ServiceInter serviceD;

    public ServiceC() {
    }
}
