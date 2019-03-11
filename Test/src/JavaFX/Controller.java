package JavaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    Pane player_view;

    @FXML
    public void foo(int n){
        System.out.println("sei dento foo()");
        btdid.setText("prova in foo");
    }

    @FXML
    public void fooclick () {
        foo(3);
    }

    @FXML
    Button btdid;
}
