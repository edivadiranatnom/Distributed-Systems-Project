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
    public Game uno;
    FXMLLoader gameLoader;
    Parent gameRoot;
    Scene gameScene;
    Stage gameStage;
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
            gameLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
            gameLoader.setController(this);
            gameRoot = gameLoader.load();

            gameScene = new Scene(gameRoot);
            gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

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
        //player.getTurno(uno);
    }
    public void setGame (Game unoLocal) {
        this.uno = unoLocal;
    }
    public void printMyDeck () {
        System.out.println("stampa del mio mazzo: \n");
        uno.stampaCarte();
    }
    public void actionMyTurn(){
        System.out.println("E' il mio turno in controller");
        // Qua la grafica per farlo giocare;
        // Richiamare su player la funzione che implementa la giocata e la valuta.
        //player.gioca(uno);
    }
}
