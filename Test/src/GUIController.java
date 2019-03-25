import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    ScrollPane myCards;
    @FXML
    HBox table;

    @FXML
    protected void startGame(ActionEvent e) throws Exception {
        player = new Player();
        uno = new Game();
        if ((inputIp.getText() != null && !inputIp.getText().isEmpty())) {
            uno = player.startPlayer(inputIp.getText(), this);
            result.setText("Connected");
            gameLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
            gameLoader.setController(this);
            gameRoot = gameLoader.load();

            gameScene = new Scene(gameRoot);
            gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            gameStage.hide();

            if (player.Client.iamleader) {
                Button btn = new Button("Distribuisci");
                btn.setLayoutX(400.0);
                btn.setLayoutY(150.0);
                btn.getStyleClass().clear();
                btn.setId("distribuisci");
                btn.getStyleClass().add("btn");
                btn.setOnAction(event -> {
                    try {
                        startDist();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                table.getChildren().add(btn);
            }
            gameStage.setScene(gameScene);
            gameStage.show();
        } else {
            result.setText("Error");
        }
    }

    @FXML
    protected void startDist() throws Exception {
        System.out.println("hai cliccato start dist");
        uno = player.distribute(uno);
    }

    public void setGame(Game unoLocal) {
        this.uno = unoLocal;
    }

    @FXML
    public void printMyDeck() {
        System.out.println("stampa del mio mazzo: \n");
        uno.stampaCarte();
        designCards(7);
    }

    @FXML
    public void designCards(int n) {
        Platform.runLater(()->{
            HBox hBox = new HBox();
            hBox.setPrefHeight(180.0);
            hBox.setPrefWidth(1080.0);
            hBox.setSpacing(15.0);
            hBox.setStyle("-fx-padding: 0 75px 0 75px");
            for (int i = 0; i<n; i++) {
                VBox vbox = new VBox();
                vbox.setStyle("-fx-background-image: url('img/cards/"+uno.MyCard.get(i).color+"/"+uno.MyCard.get(i).background+"')");
                vbox.getStyleClass().clear();
                vbox.getStyleClass().add("card_background");
                hBox.getChildren().add(vbox);
            }
            myCards.setContent(hBox);
            if(player.Client.iamleader) {
                System.out.println("remove button");
                table.getChildren().remove(table.lookup("#distribuisci"));
            }
        });
    }

    public void actionMyTurn(int isMyTurn) {
        if (isMyTurn == 1) {
            System.out.println("E' il mio turno in controller");
        }
        else {
            System.out.println("E' il turno di "+uno.giocatoreTurno);
        }
        Platform.runLater(()->{
            VBox vboxCentralCard = new VBox();
            vboxCentralCard.getStyleClass().clear();
            vboxCentralCard.getStyleClass().add("card_background");

            vboxCentralCard.setLayoutX(400.0);
            vboxCentralCard.setLayoutX(150.0);

            vboxCentralCard.setStyle("-fx-background-image: url('img/cards/"+uno.peekScarti().color+"/"+uno.peekScarti().background+"')");
            table.getChildren().add(vboxCentralCard);
        });
    }
}
