import UnoGame.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    public GUIController mioController= new GUIController();
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

    public Game testDistribution (Game uno) {
        for (int i = 0; i<7; i++){
            MyCard.add(uno.mazzo.pop());
        }
        uno.MyCard = MyCard;
        mioController.setGame(uno);
        mioController.printMyDeck();
        return uno;
    }
    public void giocaMano (Game uno) throws Exception {
        try {
            if (utility.findIp().equals(uno.giocatoreTurno)) {
                System.out.println("E' il mio turno");
                mioController.setMyTurn();
            }
            else {
                System.out.println("E' il turno di: "+uno.giocatoreTurno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
