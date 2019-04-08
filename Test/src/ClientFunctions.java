import UnoGame.*;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    public GUIController mioController;
    // public Game unoLocal = new Game();
    private static Utility utility = new Utility();
    //public ArrayList<Card> MyCard = new ArrayList<>();
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
    }

    public void setIp(ArrayList<String> ipPlayers) {
        listIpPlayer.clear();
        for (int i = 0; i < ipPlayers.size(); i++) {
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
        System.out.println("-------------------------");
    }

    public void setLeader(String leader, String myIp) throws Exception {
        String ip = myIp;
        this.leader = leader;
        if ((ip).equals(leader)) {
            System.out.println("io sono il leader: " + leader);
            this.iamleader = true;
        } else {
            System.out.println("il leader è: " + leader);
        }
    }

    public void cardDistribution(ArrayList<Card> playersCards) throws Exception {
        mioController.uno.MyCard = playersCards;
    }

    public void preStartGame(Game uno) throws Exception {
        mioController.uno.mazzo = uno.mazzo;
        mioController.uno.pushScarti(uno.peekScarti());
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
        mioController.uno.NumberAllPlayersCards = uno.NumberAllPlayersCards;
        for (String item : listIpPlayer) {
            mioController.createAvatar(item, listIpPlayer.indexOf(item));
            if((utility.findIp()+":"+mioController.player.portRegistry).equals(item)) {
                System.out.println("\npreStartGame avatar" + listIpPlayer.indexOf(item)+"\n");
                //VBox v = (VBox) mioController.gameScene.lookup("#avatar" + listIpPlayer.indexOf(item));
                //v.setStyle("-fx-border-width: 2px; -fx-border-color: grey; -fx-border-radius: 50px");
            }
        }
        //mioController.greenAvatar(listIpPlayer.get(0));

        mioController.uno.currentColor = uno.currentColor;
        try {
            if (mioController.uno.giocatoreTurno.equals(utility.findIp()+":"+mioController.player.portRegistry)) {
                mioController.designCards(7);
                mioController.uno.isMyTurn = true;
                System.out.println("E' il mio turno");
            } else {
                mioController.designCards(7);
                mioController.uno.isMyTurn = false;
                System.out.println("Non è il mio turno: "+mioController.uno.giocatoreTurno);

            }
            mioController.handlePingOnPlayerTurn();
            //mioController.pingNotTurnPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void communicationCard(Game uno, Card cartaGiocata) {
        mioController.uno.pushScarti(cartaGiocata);
        mioController.uno.giroOrario = uno.giroOrario;
        mioController.uno.currentColor = uno.currentColor;
        String rgb = "";
        if(mioController.uno.currentColor.equals("red")) rgb = "#FF5555";
        else if(mioController.uno.currentColor.equals("green")) rgb = "#55AA55";
        else if(mioController.uno.currentColor.equals("blue")) rgb = "#5654FF";
        else if(mioController.uno.currentColor.equals("yellow")) rgb = "#FFAA01";
        mioController.changeColor(rgb);
        mioController.designCardCommunicated(cartaGiocata);
    }

    public void removeDrawedCard(Game uno, String ip, int cartePescate){
        mioController.uno.mazzo = uno.mazzo;
        System.out.println("il mazzo in c.f. è lungo: "+uno.mazzo.carddeck.size());

        int numCards = Integer.parseInt(mioController.uno.NumberAllPlayersCards.get(ip).get(0));
        mioController.uno.NumberAllPlayersCards.get(ip).set(0, String.valueOf(numCards + cartePescate));

        mioController.updateAvatar(numCards+cartePescate, ip, listIpPlayer.indexOf(ip));
        System.out.println("\nGiocatore: "+ip+" ora ha "+mioController.uno.NumberAllPlayersCards.get(ip).get(0)+" carte\n");
    }

    public void communicationTurn (Game uno) {
        System.out.println("E' il turno di: "+uno.giocatoreTurno);
        mioController.removeGreenAvatar(mioController.uno.giocatoreTurno);
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
        try {
            if (uno.giocatoreTurno.equals(utility.findIp()+":"+mioController.player.portRegistry)) {

                mioController.uno.isMyTurn = true;
                System.out.println("E' il mio turno");
            } else {
                mioController.uno.isMyTurn = false;
                System.out.println("Non è il mio turno");
            }
            if(mioController.handlePingOnPlayerTurn()){
                mioController.greenAvatar(mioController.uno.giocatoreTurno);
            }
            //mioController.pingNotTurnPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCards(Game uno, String ip) throws Exception {
        mioController.uno.NumberAllPlayersCards.get(ip).set(0, String.valueOf(uno.MyCard.size()));
        mioController.updateAvatar(uno.MyCard.size(), ip, listIpPlayer.indexOf(ip));
    }


    public String ping ()  throws Exception {
        return "Pong";
    }
    public void loser(String res){
        System.out.println("\nLOSER in loser\n");
        mioController.terminate(res);
    }

}