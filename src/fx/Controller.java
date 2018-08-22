package fx;

import java.net.URL;
        import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.TextArea;
        import javafx.scene.control.TextField;

public class Controller {

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
        Button buttonStart = (Button) event.getSource();
        Scene scene = buttonStart.getScene();
        //TableColumn tableColumn = (TableColumn)scene.lookup("#tableColumnParameter");

    }

    @FXML
    void onButtonStopClick(ActionEvent event) {

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

    }
}
