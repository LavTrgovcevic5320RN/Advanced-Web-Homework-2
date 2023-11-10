package test;

import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean()
@Qualifier("ServiceD")
public class ServiceD implements ServiceInter {

    public ServiceD() {
    }
}
