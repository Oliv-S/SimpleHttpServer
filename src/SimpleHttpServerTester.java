import java.util.Observable;
import java.util.Observer;

public class SimpleHttpServerTester implements Observer {


    public static void main(String[] args) {
        int port =8080;
        if (args.length>0)
            port = Integer.parseInt(args[0]);
        SimpleHttpServerTester tester =new SimpleHttpServerTester();
        SimpleHttpServer server = SimpleHttpServer.getInstance();
        if (server.start(port)){
            System.out.println("Started");
            server.addObserver(tester);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println(SimpleHttpServer.getInstance().getRequest());
    }
}
