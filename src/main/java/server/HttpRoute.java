package server;

@SuppressWarnings("rawtypes")
public class HttpRoute {
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
        return  "Route: " + "path = '" + route + '\'' +", httpMethod = '" + httpMethod + '\'' + ", controller = " + controller + ", methodName = '" + methodName + '\'' ;
    }
}
