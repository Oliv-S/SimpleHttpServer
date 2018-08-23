package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import gui.Record;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleHttpServer extends Observable{

    private static volatile SimpleHttpServer instance;

    private HttpServer server;
    private boolean started=false;
    private List<Record> requestHeaders;

    public String getBody() {
        return body;
    }

    private String body;

    public List<Record> getRequestHeaders() {
        return Collections.unmodifiableList(requestHeaders);
    }

    private void setRequestHeadersAndBody(List<Record> requestHeaders, String body) {
        this.requestHeaders = requestHeaders;
        this.body = body;
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
        requestHeaders=null;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            if (t.getRequestURI().toString().trim().equals("/favicon.ico")) return;
            LinkedHashMap<String,List<String>> requestParameters = new LinkedHashMap<>();

            //Save request & connection parameters
            requestParameters.put("Remote Address", Collections.singletonList(t.getRemoteAddress().toString()));
            requestParameters.put("Local Address", Collections.singletonList(t.getLocalAddress().toString()));
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

            List<Record> headersList = new ArrayList<>();
            requestParameters.forEach((k,v)-> headersList.add(new Record(k,(v.size()>1)?v.toString():v.get(0))));

            StringBuilder sb = new StringBuilder();
            requestParameters.forEach((k,v)-> sb.append(k).append((v.size()>1)?v.toString():v.get(0)).append("\n"));
            sb.append("**********************************\n");
            sb.append(requestBody);


            String response = sb.toString();

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            SimpleHttpServer.getInstance().setRequestHeadersAndBody(headersList,requestBody);
        }
    }
}
