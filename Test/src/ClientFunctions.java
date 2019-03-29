import UnoGame.*;
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
        System.out.println("il mazzo passato è lungo: " + uno.mazzo.carddeck.size());
        System.out.println("e il primo scarto è: " + uno.peekScarti().card + ", " + uno.peekScarti().color);
        mioController.uno.mazzo = uno.mazzo;
        mioController.uno.pushScarti(uno.peekScarti());
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
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
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
        mioController.uno.giroOrario = uno.giroOrario;
        mioController.uno.currentColor = uno.currentColor;
        mioController.drawCardComunicated(cartaGiocata);
        System.out.println("Il turno E': "+mioController.uno.giroOrario);
        try {
            if (uno.giocatoreTurno.equals(utility.findIp()+":"+mioController.player.portRegistry)) {
                mioController.uno.isMyTurn = true;
                System.out.println("E' il mio turno in c.f.");
            } else {
                mioController.uno.isMyTurn = false;
                System.out.println("Non è il mio turno in c.f.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
