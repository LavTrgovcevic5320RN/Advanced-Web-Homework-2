package test;

import framework.annotations.*;

@Controller
public class ControllerA {

    @Autowired(verbose = true)
    private ServiceA serviceA;

    @Autowired(verbose = true)
    private ServiceB serviceB;

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private ServiceInter serviceC;

    @Path(path = "/methodA1")
    @GET
    public void methodA1(){}

    @Path(path = "/methodA2")
    @POST
    public void methodA2(){}
}
