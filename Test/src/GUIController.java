import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import UnoGame.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUIController extends VBox {
    public HashMap<String, ArrayList<Card>> CopiaCard = new HashMap<>();
    public Game uno;
    public Rules rules = new Rules();
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
    Pane paneContainer;
    @FXML
    HBox firstRow, secondRow;
    @FXML
    Button skip;

    public void playCard(Button el, HBox parent, Card cardGiocata) {
        if(uno.isMyTurn) {
            if (rules.passport(uno, cardGiocata)) {
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
                        550.0, -450.0
                );
                PathTransition transition = new PathTransition();
                transition.setNode(el);
                transition.setDuration(Duration.seconds(1.5));
                transition.setPath(polyline);
                transition.play();
                System.out.println("pos di avatar in playcard: "+player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));
                removeGreenAvatar(player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));
                try {
                    uno.currentColor = cardGiocata.color;
                    player.communicateCardPlayed(uno, cardGiocata);
                    uno.pescato = false;
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("---------------------------------\n\n Non hai giocato una carta che non potevi giocare\n\n ---------------------------------");

            }
        } else {
            System.out.println("---------------------------------\n\n Non è il tuo turno\n\n ---------------------------------");
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

        Platform.runLater(()-> {
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

            vboxCentralCard.setStyle("-fx-background-image: url('img/cards/" + uno.peekScarti().color + "/" + uno.peekScarti().background + "')");
            deck.setStyle("-fx-background-image: url('img/mazzo.png')");

            try {
                deck.setOnMousePressed(event -> {
                    try {
                        peekCard(1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            table.getChildren().add(deck);
            table.getChildren().add(vboxCentralCard);
        });

        Platform.runLater(()-> {
            /*HBox hBox = new HBox();
            hBox.setPrefHeight(400.0);
            hBox.setPrefWidth(1080.0);
            hBox.setLayoutX(0.0);
            hBox.setLayoutX(0.0);
            hBox.setSpacing(15.0);
            hBox.setId("hBox");
            hBox.setStyle("-fx-padding: 0 0px 25px 0px");

            HBox innerHBox = new HBox();
            innerHBox.setPrefHeight(180.0);
            innerHBox.setPrefWidth(1080.0);
            innerHBox.setSpacing(15.0);
            innerHBox.setLayoutX(0.0);
            innerHBox.setLayoutY(0.0);
            innerHBox.setStyle("-fx-padding: 0 75px 0 75px");
            innerHBox.setId("innerHBox1");

            hBox.getChildren().add(innerHBox);*/
            firstRow.setSpacing(15.0);
            secondRow.setSpacing(15.0);

            for (int i = 0; i < n; i++) {
                Button vbox = new Button();
                vbox.setStyle("-fx-background-image: url('img/cards/" + uno.MyCard.get(i).color + "/" + uno.MyCard.get(i).background + "')");
                vbox.getStyleClass().clear();
                vbox.getStyleClass().add("card_background");
                vbox.setId(uno.MyCard.get(i).color + " " + uno.MyCard.get(i).card);
                firstRow.getChildren().add(vbox);
                vbox.setId(uno.MyCard.get(i).color + "_" + uno.MyCard.get(i).card);
                trigEvent(vbox, firstRow, uno.MyCard.get(i));
            }

            //myCards.setContent(hBox);
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

    void designCardCommunicated(Card cardToDraw){
        System.out.println("in cima al cimitero: "+uno.peekScarti().color + ", " + uno.peekScarti().card);
        System.out.println("Il colore corrente è: "+uno.currentColor);
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
                    550.0, 150.0
            );
            PathTransition transition = new PathTransition();
            transition.setNode(card);
            transition.setDuration(Duration.seconds(1.5));
            transition.setPath(polyline);
            transition.play();
            // se è il mio turno e quello prima mi ha giocato un +2 o + 4 me le pesco.
            if (uno.isMyTurn) {
                if (cardToDraw.card == 14) {
                    System.out.println("Devi pescare 4 carte");
                    try {
                        peekCard(4);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (cardToDraw.card == 11) {
                    System.out.println("Devi pescare 2 carte");
                    try {
                        peekCard(2);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    void createAvatar(String ip, int pos){
        Platform.runLater(()-> {
            String[] parts = ip.split(":");
            String secondPart = parts[1];
            Image image = new Image("img/avatar/" + uno.NumberAllPlayersCards.get(ip).get(1));
            VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
            vBox.setAlignment(Pos.BOTTOM_CENTER);
            BackgroundSize backgroundSize = new BackgroundSize(50, 50, false, false, false, false);
            Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize));
            Text host = new Text("PORT: " + secondPart);
            Text remainingCards = new Text("CARDS: " + uno.NumberAllPlayersCards.get(ip).get(0));
            remainingCards.setId("player_"+pos);
            host.setFill(Color.WHITE);
            remainingCards.setFill(Color.WHITE);
            vBox.setBackground(background);
            vBox.getChildren().add(host);
            vBox.getChildren().add(remainingCards);
        });
    }

    @FXML
    void updateAvatar(int n, String player, int pos){
        int aggiorna = Integer.parseInt(uno.NumberAllPlayersCards.get(player).get(0));
        Text txt = (Text) gameScene.lookup("#player_"+pos);
        txt.setText("CARDS: " + String.valueOf(aggiorna));
    }

    @FXML
    private void peekCard(int n) throws Exception{
        if(uno.isMyTurn && !uno.pescato) {

            /*HBox innerHBox = new HBox();
            innerHBox.setPrefHeight(180.0);
            innerHBox.setPrefWidth(1080.0);
            innerHBox.setSpacing(15.0);
            innerHBox.setStyle("-fx-padding: 0 75px 0px 75px");
            innerHBox.setId("innerHBox2");
            innerHBox.setLayoutY(280.0);
            innerHBox.setLayoutX(0.0);
            //HBox myCards = (HBox) gameScene.lookup("#hBox");
            //myCards.getChildren().add(innerHBox);*/
            Platform.runLater(()-> {
                for (int i = 0; i < n; i++) {
                    Card c = uno.mazzo.pop();
                    uno.MyCard.add(c);

                    Button card = new Button();
                    card.getStyleClass().clear();
                    card.getStyleClass().add("card_background");
                    card.setLayoutX(200.0);
                    card.setLayoutY(150.0);
                    System.out.println("peek: " + c.color + ", " + c.card);
                    card.setStyle("-fx-background-image: url('img/cards/" + c.color + "/" + c.background + "')");
                    System.out.println("IMPILO LA CARTA\n");
                    System.out.println("HO " + uno.MyCard.size() + " carte");
                    card.setId(c.color + "_" + c.card);
                    if (uno.MyCard.size() > 7) {
                        trigEvent(card, secondRow, c);
                        secondRow.getChildren().add(card);

                    } else {
                        trigEvent(card, firstRow, c);
                        firstRow.getChildren().add(card);
                    }

                }
                ArrayList<String> arr = null;
                try {
                    arr = player.peekCardPlayer(uno, n);
                    uno.pescato = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                updateAvatar(Integer.parseInt(arr.get(1)), arr.get(0), player.listIpPlayer.indexOf(arr.get(0)));
                //player.communicateCardPlayed(uno, c);
            });

        } else {
            if (uno.pescato) {
                System.out.println("Non puoi pescare più di una carta");
            } else if (!uno.isMyTurn) { // non dire cazzate
                System.out.println("non è il tuo turno");
            }
        }
    }
    @FXML
    protected void skipAction() {
        System.out.println("Il turno ora è di "+uno.giocatoreTurno +"\n Cambia turno!");
        if (uno.isMyTurn){
            try {
                System.out.println("pos di avatar in skip: "+player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));
                removeGreenAvatar(player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));
                player.skipTurn(uno);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("non puoi skippare non è il tuo turno");
        }
    }
    public void greenAvatar(int pos) {
        System.out.println("PORCCCODDDIIIOOOOOO: "+pos);
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        vBox.setStyle("-fx-border-color: white");
    }
    public void removeGreenAvatar(int pos) {
        System.out.println("RICHIMAO UNA FUNZ SU ME STESSO");
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        vBox.setStyle("-fx-border-color: transparent");
    }

}
