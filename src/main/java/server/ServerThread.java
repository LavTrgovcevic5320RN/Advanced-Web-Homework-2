package server;

import framework.engine.DIEngine;
import framework.engine.routes.ControllerRoute;
import framework.http.response.JsonResponse;
import framework.http.response.Response;
import framework.http.request.enums.Method;
import framework.http.request.Header;
import framework.http.request.Helper;
import framework.http.request.Request;
import framework.http.request.exceptions.RequestNotValidException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final DiscoveryMechanism discoveryMechanism;
    private final DIEngine diEngine;

    public ServerThread(Socket socket){
        this.socket = socket;
        discoveryMechanism = DiscoveryMechanism.getInstance();
        diEngine = DIEngine.getInstance();

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            Request request = this.generateRequest();
            if(request == null) {
                in.close();
                out.close();
                socket.close();
                return;
            }

            for(HttpRoute httpRoute : this.discoveryMechanism.getMapOfControllerRoutes()){
                if(httpRoute.getRoute().equals(request.getLocation()) && httpRoute.getHttpMethod().equals(request.getMethod().toString())) {
                    String controllerClassName = httpRoute.getController().getName();
                    diEngine.initDependencies(controllerClassName);
                    break;
                }
            }

            // Response example
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("route_location", request.getLocation());
            responseMap.put("route_method", request.getMethod().toString());
            responseMap.put("parameters", request.getParameters());
            Response response = new JsonResponse(responseMap);
            out.println(response.render());

            in.close();
            out.close();
            socket.close();

        } catch (IOException | RequestNotValidException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Request generateRequest() throws IOException, RequestNotValidException {
        String command = in.readLine();
        if(command == null) {
            return null;
        }

        String[] actionRow = command.split(" ");
        Method method = Method.valueOf(actionRow[0]);
        String[] splittedRoute = actionRow[1].split("\\?");
        String route = splittedRoute[0];
        Header header = new Header();
        HashMap<String, String> parameters = Helper.getParametersFromRoute(actionRow[1]);

        do {
            command = in.readLine();
            String[] headerRow = command.split(": ");
            if(headerRow.length == 2) {
                header.add(headerRow[0], headerRow[1]);
            }
        } while(!command.trim().equals(""));

        if(method.equals(Method.POST)) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] buff = new char[contentLength];
            in.read(buff, 0, contentLength);
            String parametersString = new String(buff);

            if (buff.length > 0) jsonParser(parametersString, parameters);
            else parameters = new HashMap<>();
        }

        Request request = new Request(method, route, header, parameters);

        return request;
    }

    private void jsonParser(String jsonString, HashMap<String, String> parameters){
        String []str;
        str = jsonString.split("\n");

        for (int i = 1; i < str.length-1; i++) {
            String []paramsCut = (str[i].replaceAll("\"","")).split(":");
            parameters.put( paramsCut[0],  paramsCut[1]);
        }
    }
}
