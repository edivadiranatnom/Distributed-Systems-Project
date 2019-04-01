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
        }
        mioController.greenAvatar(0);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void communicationCard(Game uno, Card cartaGiocata) {
        mioController.uno.pushScarti(cartaGiocata);
        mioController.uno.giroOrario = uno.giroOrario;
        mioController.uno.currentColor = uno.currentColor;
        // communicationTurn(uno);
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
        mioController.removeGreenAvatar(listIpPlayer.indexOf(mioController.uno.giocatoreTurno));
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
        mioController.greenAvatar(listIpPlayer.indexOf(mioController.uno.giocatoreTurno));
        try {
            if (uno.giocatoreTurno.equals(utility.findIp()+":"+mioController.player.portRegistry)) {

                mioController.uno.isMyTurn = true;
                System.out.println("E' il mio turno");
            } else {
                mioController.uno.isMyTurn = false;
                System.out.println("Non è il mio turno");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}