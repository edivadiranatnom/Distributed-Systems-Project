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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
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
    Pane paneContainer, colorPanel;
    @FXML
    HBox firstRow, secondRow;
    @FXML
    Button skip;
    @FXML
    Pane outerTable;

    public void playCard(Button el, HBox parent, Card cardGiocata) {
        if(uno.isMyTurn) {
            if (rules.passport(uno, cardGiocata)) {
                uno.isMyTurn = false;
                uno.pushScarti(cardGiocata);
                uno.MyCard.remove(uno.MyCard.indexOf(cardGiocata));
                parent.getChildren().remove(el);
                el.setLayoutX(100.0);
                el.setLayoutY(700.0);
                gameMain.getChildren().add(el);
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

                if(secondRow.getChildren().size()>0 && firstRow.getChildren().size()<7){
                    int numCards = 7-firstRow.getChildren().size();
                    for (int i = 0; i<numCards; i++){
                        Node n = secondRow.getChildren().get(i);
                        firstRow.getChildren().add(n);
                    }
                }

                try {
                    player.updateMyCards(uno);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removeGreenAvatar(player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));

                if(cardGiocata.color.equals("black")){
                    System.out.println("\n\nSONO DENTROOOOO\n\n");
                    Pane chooseColor = new Pane ();
                    chooseColor.setId("chooseColor");
                    chooseColor.setLayoutX(185.0);
                    chooseColor.setLayoutY(215.0);
                    chooseColor.setPrefHeight(100.0);
                    chooseColor.setPrefWidth(100.0);
                    chooseColor.setStyle("-fx-padding: 5px 5px 5px 5px");
                    chooseColor.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: white");
                    Button c1 = new Button();
                    c1.setPrefHeight(40.0);
                    c1.setPrefWidth(40.0);
                    c1.setLayoutX(10.0);
                    c1.setLayoutY(10.0);
                    c1.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: blue");
                    chooseColor.getChildren().add(c1);
                    c1.setOnAction(event -> {
                        uno.currentColor = "blue";
                        try {
                            player.communicateCardPlayed(uno, cardGiocata);
                            changeColor(uno.currentColor);
                            Pane c = (Pane) gameScene.lookup("#chooseColor");
                            outerTable.getChildren().remove(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Button c2 = new Button();
                    c2.setPrefHeight(40.0);
                    c2.setPrefWidth(40.0);
                    c2.setLayoutX(50.0);
                    c2.setLayoutY(10.0);
                    c2.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: green");
                    chooseColor.getChildren().add(c2);
                    c2.setOnAction(event -> {
                        uno.currentColor = "green";
                        try {
                            player.communicateCardPlayed(uno, cardGiocata);
                            changeColor(uno.currentColor);
                            Pane c = (Pane) gameScene.lookup("#chooseColor");
                            outerTable.getChildren().remove(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Button c3 = new Button();
                    c3.setPrefHeight(40.0);
                    c3.setPrefWidth(40.0);
                    c3.setLayoutX(10.0);
                    c3.setLayoutY(50.0);
                    c3.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: red");
                    chooseColor.getChildren().add(c3);
                    c3.setOnAction(event -> {
                        uno.currentColor = "red";
                        try {
                            player.communicateCardPlayed(uno, cardGiocata);
                            changeColor(uno.currentColor);
                            Pane c = (Pane) gameScene.lookup("#chooseColor");
                            outerTable.getChildren().remove(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Button c4 = new Button();
                    c4.setPrefHeight(40.0);
                    c4.setPrefWidth(40.0);
                    c4.setLayoutX(50.0);
                    c4.setLayoutY(50.0);
                    c4.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: yellow");
                    chooseColor.getChildren().add(c4);
                    c4.setOnAction(event -> {
                        uno.currentColor = "yellow";
                        try {
                            player.communicateCardPlayed(uno, cardGiocata);
                            changeColor(uno.currentColor);
                            Pane c = (Pane) gameScene.lookup("#chooseColor");
                            outerTable.getChildren().remove(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    outerTable.getChildren().add(chooseColor);
                }else {
                    try {
                        uno.currentColor = cardGiocata.color;
                        changeColor(uno.currentColor);
                        player.communicateCardPlayed(uno, cardGiocata);
                        uno.pescato = false;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } else {
                System.out.println("---------------------------------\n\n Hai giocato una carta che non potevi giocare\n\n ---------------------------------");

            }
        } else {
            System.out.println("---------------------------------\n\n Non è il tuo turno\n\n ---------------------------------");
        }
    }

    @FXML
    void changeColor(String color){
        HBox c = (HBox) gameScene.lookup("#currentColor");
        c.setStyle("-fx-background-color: "+color);
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
            Text color = (Text) gameScene.lookup("#color");
            color.setFill(Color.WHITE);
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

            Button skip = new Button("Passa");
            skip.setLayoutX(725.0);
            skip.setLayoutY(175.0);
            skip.getStyleClass().clear();
            skip.setId("skipAction");
            skip.getStyleClass().add("btn");
            skip.setOnAction(event -> {
                try {
                    skipAction();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            table.getChildren().add(deck);
            table.getChildren().add(vboxCentralCard);
            outerTable.getChildren().add(skip);

        });

        Platform.runLater(()-> {
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
                    try {
                        peekCard(4);
                        System.out.println("Ho pescato 4 carte: "+uno.MyCard.size());
                        player.updateMyCards(uno);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (cardToDraw.card == 11) {
                    System.out.println("Devi pescare 2 carte");
                    try {
                        peekCard(2);
                        System.out.println("Ho pescato 2 carte: "+uno.MyCard.size());
                        player.updateMyCards(uno);

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
            host.setFont(Font.font("Roboto", 11));
            Text remainingCards = new Text("CARDS: " + uno.NumberAllPlayersCards.get(ip).get(0));
            remainingCards.setFont(Font.font("Roboto", 11));
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
                uno.pescato = false;
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

    void greenAvatar(int pos) {
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        Circle circle = new Circle(5.0f);
        circle.setFill(Color.rgb(0,255,127));
        circle.setId("turn");
        circle.setCenterY(0.0);
        Platform.runLater(()-> {
            vBox.getChildren().add(circle);
        });
    }

    void removeGreenAvatar(int pos) {
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        Circle c = (Circle) gameScene.lookup("#turn");
        Platform.runLater(()-> {
                    vBox.getChildren().remove(c);
        });
    }

}
