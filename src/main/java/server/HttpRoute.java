package server;

@SuppressWarnings("rawtypes")
public class HttpRoute {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private String route;
    private String httpMethod;
    private Class controller;
    private String methodName;

    public HttpRoute(String route, String httpMethod, Class controller, String methodName) {
        this.route = route;
        this.httpMethod = httpMethod;
        this.controller = controller;
        this.methodName = methodName;
    }

    public String getRoute() {
        return route;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Class getController() {
        return controller;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return  ANSI_YELLOW + "Route: "+ ANSI_RESET + "path = '" + route + '\'' +", httpMethod = '" + httpMethod + '\'' + ", controller = " + controller + ", methodName = '" + methodName + '\'' ;
    }
}
