package gui;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.TextArea;
        import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import server.SimpleHttpServer;

public class Controller implements Observer {
    private static ObservableList<Record> tableValues;

    public Controller() {
        super();
    }



    @Override
    public void update(Observable observable, Object o) {
        tableValues.clear();
        SimpleHttpServer.getInstance().getRequestHeaders().stream().forEach((p)-> this.tableValues.add(new Record(p.getParameter(), p.getValue())));
        textAreaBody.clear();
        try {
            textAreaBody.setText(java.net.URLDecoder.decode(SimpleHttpServer.getInstance().getBody(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="tableColumnParameter"
    private TableColumn<?, ?> tableColumnParameter; // Value injected by FXMLLoader

    @FXML // fx:id="fieldPortNumber"
    private TextField fieldPortNumber; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaBody"
    private TextArea textAreaBody; // Value injected by FXMLLoader

    @FXML // fx:id="buttonStart"
    private Button buttonStart; // Value injected by FXMLLoader

    @FXML // fx:id="buttonStop"
    private Button buttonStop; // Value injected by FXMLLoader

    @FXML // fx:id="tableColumnValue"
    private TableColumn<?, ?> tableColumnValue; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<?> table; // Value injected by FXMLLoader

    @FXML
    void onButtonStartClick(ActionEvent event) {
        //tableValues.add(new Record("A","b"));
        if (!SimpleHttpServer.getInstance().isStarted()){
            SimpleHttpServer server = SimpleHttpServer.getInstance();
            int portNumber = 8080;
            try {
                portNumber = Integer.parseInt(fieldPortNumber.getText());
            }catch (NumberFormatException e){
            }
            server.start(portNumber);
            server.addObserver(this);
        }

    }

    @FXML
    void onButtonStopClick(ActionEvent event) {
        if (SimpleHttpServer.getInstance().isStarted()) {
            SimpleHttpServer.getInstance().stop();
            SimpleHttpServer.getInstance().deleteObservers();
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tableColumnParameter != null : "fx:id=\"tableColumnParameter\" was not injected: check your FXML file 'sample.fxml'.";
        assert fieldPortNumber != null : "fx:id=\"fieldPortNumber\" was not injected: check your FXML file 'sample.fxml'.";
        assert textAreaBody != null : "fx:id=\"textAreaBody\" was not injected: check your FXML file 'sample.fxml'.";
        assert buttonStart != null : "fx:id=\"buttonStart\" was not injected: check your FXML file 'sample.fxml'.";
        assert buttonStop != null : "fx:id=\"buttonStop\" was not injected: check your FXML file 'sample.fxml'.";
        assert tableColumnValue != null : "fx:id=\"tableColumnValue\" was not injected: check your FXML file 'sample.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'sample.fxml'.";


        this.tableValues = FXCollections.observableArrayList();

        ((TableColumn)this.tableColumnParameter).setCellValueFactory(new PropertyValueFactory<Record,String>("parameter"));
        ((TableColumn) this.tableColumnValue).setCellValueFactory(new PropertyValueFactory<Record,String>("value"));
        ((TableView)this.table).setItems(tableValues);
    }

}
