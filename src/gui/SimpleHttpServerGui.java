package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;
import server.SimpleHttpServer;

public class SimpleHttpServerGui extends Application  {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        //Controller controller = loader.getController();
        //SimpleHttpServer server = SimpleHttpServer.getInstance();
        //server.addObserver(controller);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if(SimpleHttpServer.getInstance().isStarted())SimpleHttpServer.getInstance().stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
