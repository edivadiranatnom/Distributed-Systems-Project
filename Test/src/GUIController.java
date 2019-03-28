import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import UnoGame.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    public void playCard(Button el, HBox parent, Card cardGiocata) {
        if(uno.isMyTurn) {
            uno.isMyTurn = false;
            System.out.println("è il mio turno  gioco: "+cardGiocata.card + ", " + cardGiocata.color);
            uno.pushScarti(cardGiocata);
            uno.MyCard.remove(uno.MyCard.indexOf(cardGiocata));
            parent.getChildren().remove(el);
            el.setLayoutX(100.0);
            el.setLayoutY(700.0);
            gameMain.getChildren().add(el);
            // Transition
            Polyline polyline= new Polyline();

            polyline.getPoints().addAll(
                    0.0, 0.0,
                    440.0, -450.0
            );
            PathTransition transition = new PathTransition();
            transition.setNode(el);
            transition.setDuration(Duration.seconds(1.5));
            transition.setPath(polyline);
            transition.play();
            try {
                player.communicateCardPlayed(uno, cardGiocata);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Non è il tuo turno\n");
        }
    }

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
    public void designCards(int n) {
        System.out.println("stampa del mio mazzo: \n");
        uno.stampaCarte();

        Platform.runLater(()->{
            Button vboxCentralCard = new Button();
            vboxCentralCard.getStyleClass().clear();
            vboxCentralCard.getStyleClass().add("card_background");

            vboxCentralCard.setLayoutX(400.0);
            vboxCentralCard.setLayoutX(150.0);

            vboxCentralCard.setStyle("-fx-background-image: url('img/cards/"+uno.peekScarti().color+"/"+uno.peekScarti().background+"')");
            table.getChildren().add(vboxCentralCard);
        });
        Platform.runLater(() -> {
            HBox hBox = new HBox();
            hBox.setPrefHeight(180.0);
            hBox.setPrefWidth(1080.0);
            hBox.setSpacing(15.0);
            hBox.setStyle("-fx-padding: 0 75px 0 75px");
            hBox.setId("hBox");
            for (int i = 0; i < n; i++) {
                Button vbox = new Button();
                vbox.setStyle("-fx-background-image: url('img/cards/" + uno.MyCard.get(i).color + "/" + uno.MyCard.get(i).background + "')");
                vbox.getStyleClass().clear();
                vbox.getStyleClass().add("card_background");
                hBox.getChildren().add(vbox);
                vbox.setId(uno.MyCard.get(i).color+"_"+uno.MyCard.get(i).card);
                trigEvent(vbox, hBox, uno.MyCard.get(i));
            }
            myCards.setContent(hBox);
            if (player.Client.iamleader) {
                System.out.println("remove button");
                table.getChildren().remove(table.lookup("#distribuisci"));
            }
        });
    }
    void trigEvent(Button vbox, HBox hBox, Card c){
        vbox.setOnMousePressed(event -> {
            System.out.println("Ho cliccato su: " + ((Control)event.getSource()).getId()+"\n");
            try {
                playCard(vbox, hBox, c);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    void drawCardComunicated(Card cardToDraw){
        System.out.println("la prima del cimitero: "+uno.peekScarti().color + ", " + uno.peekScarti().card);
        Platform.runLater(()-> {
            Button card = new Button();
            card.getStyleClass().clear();
            card.getStyleClass().add("card_background");
            card.setLayoutX(100.0);
            card.setLayoutY(100.0);
            card.setStyle("-fx-background-image: url('img/cards/" + cardToDraw.color + "/" + cardToDraw.background + "')");
            gameMain.getChildren().add(card);
            Polyline polyline = new Polyline();

            polyline.getPoints().addAll(
                    0.0, 0.0,
                    440.0, 150.0
            );
            PathTransition transition = new PathTransition();
            transition.setNode(card);
            transition.setDuration(Duration.seconds(1.5));
            transition.setPath(polyline);
            //transition.setCycleCount(PathTransition.INDEFINITE);
            transition.play();
        });
    }
}
