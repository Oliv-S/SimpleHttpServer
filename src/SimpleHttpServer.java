import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {
        if (args.length<1){
            System.out.println("Usage: SimpleHttpServer port\r\nExample: SimpleHttpServer 8080");
            return;
        }
        System.out.println("Server is ready:\r\n");
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers headers = t.getRequestHeaders();

            Set set = headers.entrySet();

            StringBuilder sb = new StringBuilder();
            sb.append("Client IP: " + t.getRemoteAddress()+"\r\n");
            sb.append("Server IP: " + t.getLocalAddress()+"\r\n");
            sb.append("URL: " + t.getRequestURI()+"\r\n");
            sb.append("Protocol: " + t.getProtocol()+"\r\n");
            sb.append("Method: " + t.getRequestMethod()+"\r\n");
            String requestBody = new BufferedReader(new InputStreamReader(t.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));
            if (requestBody.trim().length()>0)
                sb.append("RequestBody: " + requestBody +"\r\n");

            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                sb.append(entry.getKey() + " = " + entry.getValue());
                sb.append("\r\n");
            }
            System.out.println(sb.toString());
            System.out.println("**********************************\r\n");
            String response = "This is the response: \r\n" + sb.toString();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }
}
