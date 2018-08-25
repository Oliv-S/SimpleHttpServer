package test;
import gui.SimpleHttpServerGui;
import javafx.application.Application;

import java.util.Arrays;

public class Start {

    public static void main(String[] args) {
        if (args.length>0){
            SimpleHttpServerTester.main(args);
        }else{
            System.out.println("No arguments");
            System.out.println("Starting in GUI mode.");
            System.out.println("For command line mode use:java -jar SimpleHttpServer.jar port_number");
            Application.launch(SimpleHttpServerGui.class, args);
        }
    }
}
