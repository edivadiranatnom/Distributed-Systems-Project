import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import UnoGame.*;
import javafx.stage.Stage;

public class GUIController extends VBox {
    public HashMap<String, ArrayList<Card>> CopiaCard = new HashMap<>();
    Game uno;
    Player player;
    @FXML
    TextField inputIp;
    @FXML
    Label result;
    @FXML
    AnchorPane gameMain;


    @FXML
    protected void startGame(ActionEvent e) throws Exception{
        player = new Player();
        if ((inputIp.getText() != null && !inputIp.getText().isEmpty())) {
            uno = player.startPlayer(inputIp.getText());
            result.setText("Connected");
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
            gameLoader.setController(this);
            Parent gameRoot = gameLoader.load();

            Scene gameScene = new Scene(gameRoot);
            Stage gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            gameStage.hide();

            if(player.Client.iamleader){
                Button btn = new Button("Distribuisci");
                btn.setLayoutX(492.0);
                btn.setLayoutY(527.0);
                btn.setOnAction(event -> {
                    try {
                        startDist();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                gameMain.getChildren().add(btn);
            }

            gameStage.setScene(gameScene);
            gameStage.show();
        }else{
            result.setText("Error");
        }
    }
    @FXML
    protected void startDist() throws Exception {
        uno = player.distribute(uno);
    }
    public void setGame (Game uno) {
        this.uno = uno;
    }
    public void printMyDeck () {
        System.out.println("stampa del mio mazzo: \n");
        for (int i = 0; i < uno.MyCard.size(); i++) {
            System.out.println(uno.MyCard.get(i).card +", "+uno.MyCard.get(i).color);
        }
    }
}
