import UnoGame.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    public GUIController mioController= new GUIController();
    public Game unoLocal = new Game();
    private static Utility utility = new Utility();
    public ArrayList<Card> MyCard = new ArrayList<>();
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
    }

    public void setIp(ArrayList<String> ipPlayers) {
        listIpPlayer.clear();
        for (int i=0; i<ipPlayers.size(); i++) {
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
        System.out.println("-------------------------");
    }

    public void setLeader(String leader) throws Exception{
        String ip = utility.findIp();
        this.leader = leader;
        if ((ip).equals(leader)){
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
        } else {
            System.out.println("il leader è: "+leader);
        }
    }

    public int cardDistribution (ArrayList<Card> playersCards) throws Exception{
        unoLocal.MyCard = playersCards;
        mioController.setGame(unoLocal);
        mioController.printMyDeck();
        return 1;
    }
    public void communicateTurno (Game uno) throws Exception {
        try {
            if (utility.findIp().equals(uno.giocatoreTurno)) {
                System.out.println("E' il mio turno");
                mioController.actionMyTurn();
            }
            else {
                System.out.println("E' il turno di: "+uno.giocatoreTurno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preStartGame (Game uno) throws Exception {
        System.out.println("il mazzo passato è lungo: "+uno.mazzo.carddeck.size());
        mioController.uno.mazzo = uno.mazzo;
        mioController.uno.pushScarti(uno.peekScarti());
        mioController.uno.giocatoreTurno = uno.giocatoreTurno;
        try {
            if(mioController.uno.giocatoreTurno.equals(utility.findIp())) {
                System.out.println("E' il mio turno");
                mioController.actionMyTurn();
            } else {
                System.out.println("E' il turno di: "+uno.giocatoreTurno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
