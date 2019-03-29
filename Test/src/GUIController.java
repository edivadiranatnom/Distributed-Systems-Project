import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import UnoGame.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

import static javafx.scene.layout.BorderWidths.AUTO;

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

    public void playCard(Button el, HBox parent, Card cardGiocata, int isMyTurn) {
        if(isMyTurn == 1) {

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
    public void designCards(int n, int isMyTurn) {
        Platform.runLater(()->{
            Button vboxCentralCard = new Button();
            Button deck = new Button();
            vboxCentralCard.getStyleClass().clear();
            vboxCentralCard.getStyleClass().add("card_background");

            deck.getStyleClass().clear();
            deck.getStyleClass().add("deck_background");

            vboxCentralCard.setLayoutX(400.0);
            vboxCentralCard.setLayoutY(150.0);

            deck.setLayoutX(200.0);
            deck.setLayoutY(150.0);

            vboxCentralCard.setStyle("-fx-background-image: url('img/cards/"+uno.peekScarti().color+"/"+uno.peekScarti().background+"')");
            deck.setStyle("-fx-background-image: url('img/mazzo.png')");
            table.getChildren().add(deck);
            table.getChildren().add(vboxCentralCard);

            HBox hBox = new HBox();
            hBox.setPrefHeight(180.0);
            hBox.setPrefWidth(1080.0);
            hBox.setSpacing(15.0);
            hBox.setStyle("-fx-padding: 0 75px 0 75px");
            hBox.setId("hBox");
            int i;
            for (i = 0; i < n; i++) {
                Button vbox = new Button();
                vbox.setStyle("-fx-background-image: url('img/cards/" + uno.MyCard.get(i).color + "/" + uno.MyCard.get(i).background + "')");
                vbox.getStyleClass().clear();
                vbox.getStyleClass().add("card_background");
                vbox.setId(uno.MyCard.get(i).color+" "+uno.MyCard.get(i).card);
                hBox.getChildren().add(vbox);
                foo(vbox, hBox, uno.MyCard.get(i), isMyTurn);
            }
            myCards.setContent(hBox);
            if (player.Client.iamleader) {
                table.getChildren().remove(table.lookup("#distribuisci"));
            }
        });
    }

    void foo(Button vbox, HBox hBox, Card c, int isMyTurn){
        vbox.setOnMousePressed(event -> {
            System.out.println("Ho cliccato su: " + ((Control)event.getSource()).getId()+"\n");
            try {
                playCard(vbox, hBox, c, isMyTurn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    void drawCardComunicated(Card cardToDraw) {
        System.out.println("la prima del cimitero: " + uno.peekScarti().color + ", " + uno.peekScarti().card);
        Platform.runLater(() -> {
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
            transition.play();
        });
    }

    @FXML
    void createAvatar(String ip, int pos){
        Platform.runLater(()-> {
            String[] parts = ip.split(":");
            String secondPart = parts[1];
            Image image = new Image("img/avatar/"+uno.NumberAllPlayersCards.get(ip).get(1));
            VBox vBox = (VBox) gameScene.lookup("#avatar"+pos);
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            BackgroundSize backgroundSize = new BackgroundSize(50, 50, false, false, false, false);
            Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize));
            Text host = new Text("PORT: "+secondPart);
            Text remainingCards = new Text("CARDS: "+uno.NumberAllPlayersCards.get(ip).get(0));
            host.setFill(Color.WHITE);
            remainingCards.setFill(Color.WHITE);
            vBox.setBackground(background);
            vBox.getChildren().add(host);
            vBox.getChildren().add(remainingCards);
        });
    }

}
