import java.util.Observable;
import java.util.Observer;

public class SimpleHttpServerTester implements Observer {


    public static void main(String[] args) {
        int port =8080;
        if (args.length>0)
            try{
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Port number incorrect: "+args[0]);
                return;
            }

        SimpleHttpServerTester tester =new SimpleHttpServerTester();
        SimpleHttpServer server = SimpleHttpServer.getInstance();
        if (server.start(port)){
            System.out.println("Server started on port: " + port);
            server.addObserver(tester);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println(SimpleHttpServer.getInstance().getRequest());
    }
}
