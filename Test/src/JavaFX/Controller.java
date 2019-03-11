package JavaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller {
    @FXML
    Pane player_view;

    public void foo(int n){
        GridPane gp = new GridPane();
        gp.getStyleClass().add("my-card");
        player_view.getChildren().add(gp);
    }

    @FXML
    public void fooclick () {
        foo(3);
    }
}
