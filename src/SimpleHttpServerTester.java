import java.util.Observable;
import java.util.Observer;

public class SimpleHttpServerTester implements Observer {


    public static void main(String[] args) {
        SimpleHttpServerTester tester =new SimpleHttpServerTester();
        SimpleHttpServer server = SimpleHttpServer.getInstance();
        if (server.start(8080)){
            System.out.println("Started");
            server.addObserver(tester);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println(SimpleHttpServer.getInstance().getRequest());
    }
}
