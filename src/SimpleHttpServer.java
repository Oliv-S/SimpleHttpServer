import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleHttpServer extends Observable{

    private static volatile SimpleHttpServer instance;

    private HttpServer server;
    private boolean started=false;
    private String request;

    public String getRequest() {
        return this.request;
    }

    private void setRequest(String request) {
        this.request = request;
        setChanged();
        notifyObservers();
    }

    private SimpleHttpServer() {

    }


    public static SimpleHttpServer getInstance() {
        if(instance == null) {
            synchronized(SimpleHttpServer.class) {
                if(instance == null) {
                    instance = new SimpleHttpServer();
                }
            }
        }
        return instance;
    }

    public boolean start(int port){
        if (port<1||port>65535) return false;
        this.started=true;
        try {
            this.server = HttpServer.create();
            server.bind(new InetSocketAddress(port),0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();

        }catch (BindException e){
            //the server cannot bind to the requested address or if the server is already bound.
            this.started=false;
            System.out.println("the server cannot bind to the requested address or if the server is already bound. " +e.getMessage() );
        }catch (NullPointerException e){
            //addr is null
            System.out.println("addr is null. " +e.getMessage() );
        }catch (IllegalArgumentException e){
            //the port parameter is outside the specified range of valid port values
            this.started=false;
            System.out.println("the port parameter is outside the specified range of valid port values. " +e.getMessage() );
        }catch (IllegalStateException e){
            this.started=false;
            System.out.println("the server is already started. " +e.getMessage() );
        }catch (IOException e){
            this.started=false;
            System.out.println(e.getMessage() );
        }finally {
            if (!started)
                server=null;
        }
        return started;
    }

    public boolean isStarted(){
        return started;
    }

    public void stop(){
        server.stop(0);
        server=null;
        started = false;
        request=null;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            LinkedHashMap<String,List<String>> requestParameters = new LinkedHashMap<>();

            //Save request & connection parameters
            requestParameters.put("Remote IP", Collections.singletonList(t.getRemoteAddress().toString()));
            requestParameters.put("URL: " , Collections.singletonList( t.getRequestURI().toString()));
            requestParameters.put("Protocol: " , Collections.singletonList( t.getProtocol()));
            requestParameters.put("Method: " , Collections.singletonList( t.getRequestMethod()));

            Headers headers = t.getRequestHeaders();
            //Save request headers
            Set<Map.Entry<String, List<String>>> set = headers.entrySet();
            for (Map.Entry<String, List<String>> entry : set) {
                requestParameters.put(entry.getKey(),  entry.getValue());
            }

            //Save request Body
            String requestBody = new BufferedReader(new InputStreamReader(t.getRequestBody()))
                    .lines().collect(Collectors.joining("\n")).trim();

            StringBuilder sb = new StringBuilder();
            requestParameters.forEach((k,v)-> sb.append(k).append((v.size()>1)?v.toString():v.get(0)).append("\n"));
            sb.append("**********************************\n");
            sb.append(requestBody);


            String response = sb.toString();
            //System.out.println(response);
            SimpleHttpServer.getInstance().setRequest(response);


            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
