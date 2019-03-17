import UnoGame.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    private static Utility utility = new Utility();
    public ArrayList<Card> MyCard = new ArrayList<>();
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
        //new Thread(() -> Application.launch(GUI.class)).start();
    }
    public void getIp(ArrayList<String> ipPlayers) {
        listIpPlayer.clear();
        for (int i=0; i<ipPlayers.size(); i++) {
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
        System.out.println("-------------------------");
    }
    public void electionLeader(String leader) throws Exception{
        String ip = utility.findIp();
        this.leader = leader;
        int nPlayers = listIpPlayer.size();
        if ((ip).equals(leader)){
            Game uno = new Game();
            Deck deck = uno.shuffle();
            Deck tmpDeck;
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
            // circular array
            int miaPos = listIpPlayer.indexOf(leader);

            for (int i = miaPos + 1; i < nPlayers + miaPos + 1; i++) {
                PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
                tmpDeck = stubPlayer.testDistribution(deck, uno, ip);
                //System.out.println("ora il deck è lungo parte 2:"+tmpDeck.carddeck.size());
                deck = tmpDeck;
            }
        } else {
            System.out.println("il leader è: "+leader);
        }
        System.out.println("stampa del mio mazzo");
        for (int i = 0; i<7; i++) {
            System.out.println(MyCard.get(i).card + " " + MyCard.get(i).color + "\n");
        }
    }

    public Deck testDistribution (Deck deck, Game uno, String ip) {
        //System.out.println("il deck era lungo: "+deck.carddeck.size());
        for (int i = 0; i<7; i++){
            MyCard.add(deck.pop());
        }
        uno.AllPlayersCards.put(ip, MyCard);
        //uno.stampa(ip);
        System.out.println("-------------------------");
        System.out.println("ora deck è lungo: "+deck.carddeck.size());
        System.out.println("-------------------------");
        return deck;
    }

}
