import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import UnoGame.*;
import javafx.stage.Stage;

public class GUIController extends VBox {
    public HashMap<String, ArrayList<Card>> CopiaCard = new HashMap<>();

    @FXML
    TextField inputIp;
    @FXML
    Label result;

    @FXML
    void startGame(ActionEvent e) throws Exception{

        Player player = new Player();
        if ((inputIp.getText() != null && !inputIp.getText().isEmpty())) {
            player.startPlayer(inputIp.getText());
            result.setText("Connected");

            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
            gameLoader.setController(this);
            Parent gameRoot = gameLoader.load();

            Scene gameScene = new Scene(gameRoot);
            Stage gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            gameStage.hide();

            gameStage.setScene(gameScene);
            gameStage.show();

        }else{
            result.setText("Error");
        }

    }

    @FXML
    void startDist(ActionEvent e) throws Exception{ }
}
