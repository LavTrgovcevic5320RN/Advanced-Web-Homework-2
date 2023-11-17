package test;

import framework.annotations.*;

@Controller
public class ControllerB {

    @Autowired(verbose = true)
    private ServiceA serviceA;

    @Autowired(verbose = true)
    private ServiceB serviceB;

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private Service serviceC;

    @Path(path = "/methodB1")
    @GET
    public void methodB1(){}

    @Path(path = "/methodB2")
    @POST
    public void methodB2(){}
}
