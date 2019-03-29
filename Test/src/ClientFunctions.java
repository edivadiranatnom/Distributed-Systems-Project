import UnoGame.*;
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

        try {
            if (mioController.uno.giocatoreTurno.equals(utility.findIp()+":"+mioController.player.portRegistry)) {
                mioController.designCards(7,1);
            } else {
                mioController.designCards(7,0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void communicationCard(Card cartaGiocata) {
        mioController.uno.pushScarti(cartaGiocata);
        mioController.drawCardComunicated(cartaGiocata);
    }

}