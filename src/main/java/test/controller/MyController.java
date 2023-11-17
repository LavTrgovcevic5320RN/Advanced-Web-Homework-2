package test.controller;

import framework.annotations.*;
import framework.http.request.Request;
import framework.http.response.JsonResponse;
import framework.http.response.Response;
import test.bean.Bean1;
import test.component.Component1;
import test.qualifier.Interface1;
import test.qualifier.Interface2;
import test.service.Service1;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MyController {

    private final String controllerName = "Controller";

    @Autowired(verbose = false)
    private Component1 component1;

    @Autowired(verbose = false)
    private Bean1 bean1;

    @Autowired(verbose = false)
    private Service1 service1;

    @Autowired(verbose = true)
    @Qualifier(value = "interface1")
    private Interface1 interface1;

    @Autowired(verbose = false)
    @Qualifier(value = "interface2")
    private Interface2 interface2;

    public MyController() {

    }

    @GET
    @Path(path = "/")
    public Response root(Request request) {
        System.out.println(controllerName + ": ROOT");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", request.getLocation());
        responseMap.put("route_method", request.getMethod().toString());
        responseMap.put("parameters", request.getParameters());

        return new JsonResponse(responseMap);
    }

    @GET
    @Path(path = "/method1")
    public void method1() {
        System.out.println(controllerName + ": METHOD1 - GET");
    }

    @POST
    @Path(path = "/method2")
    public Response method2(Request request) {
        System.out.println(controllerName + ": METHOD2 - POST");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("route_location", request.getLocation());
        responseMap.put("route_method", request.getMethod().toString());
        responseMap.put("parameters", request.getParameters());

        return new JsonResponse(responseMap);
    }
}
