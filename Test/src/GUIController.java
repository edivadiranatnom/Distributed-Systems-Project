import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

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
    private static Utility utility = new Utility();
    public Rules rules = new Rules();
    FXMLLoader gameLoader;
    Parent gameRoot;
    Scene gameScene;
    Stage gameStage;
    Player player;
    Timer pingTimer = new Timer();
    PlayerInterface stub;
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

    public void terminate(String res){
        try {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("End Game");
                if (res.equals("winner")) {
                    alert.setContentText("Hai vinto!");
                    System.out.println("\nHai vinto\n");
                } else if (res.equals("loser")) {
                    alert.setContentText("Hai perso :(");
                    System.out.println("\nHai perso\n");
                }
                alert.showAndWait();
                System.out.println("\nFINE PARTITA\n");
                gameStage.close();
            });
        }catch(Exception e){}
    }

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

                if(uno.MyCard.isEmpty()){
                    try {
                        player.terminate(uno, "loser");
                        System.out.println("\nLOSER in playCard\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nWINNER in playCard\n");
                    terminate("winner");
                }

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

                if (uno.pescato){
                    Button b = (Button) gameScene.lookup("#skipAction");
                    outerTable.getChildren().remove(b);
                }

                if(cardGiocata.color.equals("black")){
                    Pane chooseColor = new Pane ();
                    chooseColor.setId("chooseColor");
                    chooseColor.setLayoutX(185.0);
                    chooseColor.setLayoutY(215.0);
                    chooseColor.setPrefHeight(100.0);
                    chooseColor.setPrefWidth(100.0);
                    chooseColor.setStyle("-fx-padding: 5px 5px 5px 5px");
                    chooseColor.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: white");

                    Button c1 = new Button();
                    colorClick(c1, "blue", cardGiocata, 10.0, 10.0);
                    chooseColor.getChildren().add(c1);

                    Button c2 = new Button();
                    colorClick(c2, "green", cardGiocata, 50.0, 10.0);
                    chooseColor.getChildren().add(c2);

                    Button c3 = new Button();
                    colorClick(c3, "red", cardGiocata, 10.0, 50.0);
                    chooseColor.getChildren().add(c3);

                    Button c4 = new Button();
                    colorClick(c4, "yellow", cardGiocata, 50.0, 50.0);
                    chooseColor.getChildren().add(c4);

                    outerTable.getChildren().add(chooseColor);
                }else {
                    try {
                        uno.currentColor = cardGiocata.color;
                        String rgb = "";
                        if(uno.currentColor.equals("red")) rgb = "#FF5555";
                        else if(uno.currentColor.equals("green")) rgb = "#55AA55";
                        else if(uno.currentColor.equals("blue")) rgb = "#5654FF";
                        else if(uno.currentColor.equals("yellow")) rgb = "#FFAA01";
                        changeColor(rgb);
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
    void colorClick(Button button, String color, Card card, double X, double Y){
        button.setPrefHeight(40.0);
        button.setPrefWidth(40.0);
        button.setLayoutX(X);
        button.setLayoutY(Y);
        String rgb = "";
        if(color.equals("red")) rgb = "#FF5555";
        else if(color.equals("green")) rgb = "#55AA55";
        else if(color.equals("blue")) rgb = "#5654FF";
        else if(color.equals("yellow")) rgb = "#FFAA01";
        button.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-background-color: "+rgb);
        String finalRgb = rgb;
        button.setOnAction(event -> {
            uno.currentColor = color;
            try {
                player.communicateCardPlayed(uno, card);
                changeColor(finalRgb);
                Pane c = (Pane) gameScene.lookup("#chooseColor");
                outerTable.getChildren().remove(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void changeColor(String color){
        HBox c = (HBox) gameScene.lookup("#currentColor");
        c.setStyle("-fx-background-color: "+color+"; -fx-background-radius: 5px");
    }

    @FXML
    protected void startGame(ActionEvent e) throws Exception {

        player = new Player();
        uno = new Game();
        if ((inputIp.getText() != null && !inputIp.getText().isEmpty() && (inputIp.getText().contains("192.168") || inputIp.getText().contains("10.") || inputIp.getText().contains("172.")))) {
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

            Card scarto = uno.peekScarti();

            vboxCentralCard.setStyle("-fx-background-image: url('img/cards/" + scarto.color + "/" + scarto.background + "')");
            deck.setStyle("-fx-background-image: url('img/mazzo.png')");

            try {
                deck.setOnMousePressed(event -> {
                    if(uno.isMyTurn) drawSkip();
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

            Pane p =  (Pane) gameScene.lookup("#parentColor");
            HBox h = new HBox();
            h.setPrefHeight(10.0);
            h.setPrefWidth(50.0);
            h.setLayoutX(0.0);
            h.setLayoutY(0.0);
            h.setStyle("-fx-alignment: center");
            Text t = new Text("COLOR");
            t.setId("color");
            t.setFill(Color.WHITE);
            HBox hb = new HBox();
            hb.setId("currentColor");
            hb.setPrefHeight(40.0);
            hb.setPrefWidth(50.0);
            hb.setLayoutX(0.0);
            hb.setLayoutY(15.0);
            String rgb = "";
            if(scarto.color.equals("red")) rgb = "#FF5555";
            else if(scarto.color.equals("green")) rgb = "#55AA55";
            else if(scarto.color.equals("blue")) rgb = "#5654FF";
            else if(scarto.color.equals("yellow")) rgb = "#FFAA01";
            hb.setStyle("-fx-border-width: 1px; -fx-border-color: grey; -fx-border-radius: 5px; -fx-background-radius: 5px ; -fx-background-color: "+rgb);
            h.getChildren().add(t);
            p.getChildren().add(h);
            p.getChildren().add(hb);

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

    void drawSkip(){
        Button skip = new Button("Passa");
        skip.setLayoutX(725.0);
        skip.setLayoutY(175.0);
        skip.getStyleClass().clear();
        skip.setId("skipAction");
        skip.getStyleClass().add("btn");
        skip.setOnAction(e -> {
            try {
                skipAction();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        outerTable.getChildren().add(skip);
    }

    void trigEvent(Button vbox, HBox hBox, Card c){
        vbox.setOnMousePressed(event -> {
            System.out.println("Ho cliccato su: " + ((Control)event.getSource()).getId()+"\n");
            if(c.card != uno.peekScarti().card || (!c.color.equals(uno.peekScarti().color))){
                Tooltip tooltip = new Tooltip();
                tooltip.setText("\nNon puoi giocare questa carta\n");
                vbox.setTooltip(tooltip);
            }
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
                    if( n==2 || n==4) {
                        VBox v = (VBox) gameScene.lookup("#avatar"+player.listIpPlayer.indexOf(uno.giocatoreTurno));
                        Circle c = (Circle) gameScene.lookup("#turn");
                        v.getChildren().remove(c);
                        player.skipTurn(uno);
                    }

                    uno.pescato = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateAvatar(Integer.parseInt(arr.get(1)), arr.get(0), player.listIpPlayer.indexOf(arr.get(0)));
            });

        } else {
            if (uno.pescato) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Draw Error");
                alert.setContentText("Non puoi pescare più di una carta");
                alert.showAndWait();
                System.out.println("Non puoi pescare più di una carta");
            } else if (!uno.isMyTurn) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Turn Error");
                alert.setContentText("Non è il tuo turno");
                alert.showAndWait();
                System.out.println("non è il tuo turno");
            }
        }
    }
    @FXML
    protected void skipAction() {
        System.out.println("Il turno ora è di "+uno.giocatoreTurno +"\n Cambia turno!");
        if (uno.isMyTurn){
            try {
                Button b = (Button) gameScene.lookup("#skipAction");
                outerTable.getChildren().remove(b);
                uno.pescato = false;
                removeGreenAvatar(player.Client.listIpPlayer.indexOf(uno.giocatoreTurno));
                player.skipTurn(uno);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skip Error");
            alert.setContentText("Non puoi passare se non è il tuo turno");
            alert.showAndWait();

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

    void redAvatar(int pos) {
        System.out.println("E' morto l'avatar in posizione "+pos);
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        Circle circle = new Circle(5.0f);
        circle.setFill(Color.rgb(255,0,0));
        circle.setId("circle_death_"+pos);
        circle.setCenterY(0.0);
        Platform.runLater(()-> {
            vBox.getChildren().add(circle);
        });
        vBox.setId("death"+pos);
        for (int i = 0; i<player.listIpPlayer.size(); i++) {
            if(i>pos) {
                VBox tmp = (VBox) gameScene.lookup("#avatar" + i);
                tmp.setId("avatar"+(i-1));
            }
        }
    }

    void updateAvatarTextPos(int pos) {
        Text txt = (Text) gameScene.lookup("#player_" + pos);
        txt.setId("death_text"+pos);
        for (int i = 0; i<player.listIpPlayer.size(); i++) {
            if(i>pos) {
                Text tmp = (Text) gameScene.lookup("#player_" + i);
                tmp.setId("player_"+(i-1));
            }
        }
    }

    void removeGreenAvatar(int pos) {
        VBox vBox = (VBox) gameScene.lookup("#avatar" + pos);
        Circle c = (Circle) gameScene.lookup("#turn");
        System.out.println("\nvBox id: "+vBox.getId()+"\n");
        Platform.runLater(()-> {
                    vBox.getChildren().remove(c);
        });
    }

    void handlePingOnPlayerTurn () throws Exception{
        try {
            pingTimer.cancel();
            pingTimer.purge();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        stub = (PlayerInterface) Naming.lookup("rmi://"+ uno.giocatoreTurno + "/ciao");

        if (!uno.isMyTurn) {
            try {
                stub.ping();
                pingTimer = new Timer();
                System.out.println("\nBefore schedule in handlePingOnPlayerTurn\n");
                pingTimer.schedule(new Ping(pingTimer, stub, this), 0, 1000);
            } catch (Exception e) {
                System.out.println("Il giocatore si è disconnesso in PING");
                bar();
            }
        }
    }

    void bar() {
        // controlla che ci siano alemno 2/3 player.
        // riassegna il turno.
        // elimina il player caduto da player.listIpPlayer e da  player.NumberAllPlayersCards
        // togli avatar
        // update grafica con pallino verde ecc..

        System.out.println("Il turnista è caduto: "+uno.giocatoreTurno);
        uno.numAllDeath ++;


        // CAMBIO TURNO
        String playerDead = uno.giocatoreTurno;

        // CONTROLLO CHE CI SIANO ALMENO 2/3 PLAYER.
        if (player.listIpPlayer.size() < 3) {
            System.out.println("Non ci sono abbstanza giocatori. MI CHIUDO");
            System.exit(0);
        }

        removeGreenAvatar(player.listIpPlayer.indexOf(uno.giocatoreTurno)); // tolgo il pallino verde a quello che è caduto
        redAvatar(player.listIpPlayer.indexOf(uno.giocatoreTurno)); // aggiungo il pallino rosso a quello che è caduto
        updateAvatarTextPos(player.listIpPlayer.indexOf(uno.giocatoreTurno)); //aggiorno l'id del testo degli avatar
        int nPlayers = player.listIpPlayer.size();
        int myIndex = player.listIpPlayer.indexOf(uno.giocatoreTurno);
        if (uno.giroOrario) {
            uno.giocatoreTurno = player.listIpPlayer.get((myIndex + 1) % nPlayers);
        } else {
            if(myIndex == 0){
                System.out.println("Sono lo 0. Setto a mano l'ultimo");
                uno.giocatoreTurno = player.listIpPlayer.get(player.listIpPlayer.size()-1);
            }
            else {
                uno.giocatoreTurno = player.listIpPlayer.get((myIndex-1)%nPlayers);
            }
        }
        try {
            if (uno.giocatoreTurno.equals(utility.findIp()+":"+player.portRegistry)) {

                uno.isMyTurn = true;
                System.out.println("E' il mio turno");
            } else {
                uno.isMyTurn = false;
                System.out.println("Non è il mio turno, è il turno di: "+uno.giocatoreTurno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // RIMUOVO IL PLAYER CADUTO
        System.out.println("prima c'erano "+player.listIpPlayer.size());
        player.listIpPlayer.remove(playerDead);
        System.out.println("ora ce ne sono "+player.listIpPlayer.size());
        System.out.println("\nPre bar()\n");
        greenAvatar((player.listIpPlayer.indexOf(uno.giocatoreTurno)));
        System.out.println("Post bar()\n");
        // riparte
        try {
            handlePingOnPlayerTurn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void pingNotTurnPlayer () throws Exception{
        System.out.println("\nStart pingNotTurnPlayer\n");
        ArrayList<String> tmpListIp = new ArrayList<>();
        for(int i = 0; i<player.listIpPlayer.size(); i++) {
            System.out.println("\nip: "+player.listIpPlayer.get(i)+"\n");
            if(!player.listIpPlayer.get(i).equals(uno.giocatoreTurno)) {
                System.out.println("\nDentro if\nip: "+player.listIpPlayer.get(i)+"\n");
                try {
                    stub = (PlayerInterface) Naming.lookup("rmi://" + player.listIpPlayer.get(i) + "/ciao");
                    stub.ping();
                } catch (Exception e) {
//                    removeGreenAvatar(player.listIpPlayer.indexOf(player.listIpPlayer.get(i)));
                    redAvatar(player.listIpPlayer.indexOf(player.listIpPlayer.get(i)));
                    updateAvatarTextPos(player.listIpPlayer.indexOf(player.listIpPlayer.get(i)));
                    System.out.println("Un player di cui non era il turno è caduto");
                    continue;
                }
            }
            tmpListIp.add(player.listIpPlayer.get(i));
        }
        player.listIpPlayer = tmpListIp;
        System.out.println("Updated listIpPlayer\n");
        for (String item : player.listIpPlayer){
            System.out.println(item+"\n");
        }
    }
}
