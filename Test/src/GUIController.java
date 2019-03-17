import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import UnoGame.*;

public class GUIController extends VBox {
    public HashMap<String, ArrayList<Card>> CopiaCard = new HashMap<>();
    @FXML
    TextField inputIp;
    @FXML
    Label result;

    GUIController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Uno.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    protected void startGame() throws Exception{
        Player player = new Player();

        if ((inputIp.getText() != null && !inputIp.getText().isEmpty())) {
            player.startPlayer(inputIp.getText());
            result.setText("Connected");
        }else{
            result.setText("Error");
        }
    }
}
