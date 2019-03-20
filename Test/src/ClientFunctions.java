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

    /*
    *   Ad ogni nuova connessione, pulisce e aggiorna la lista
    *   degli ip di ogni player
    */
    public void setIp(ArrayList<String> ipPlayers) {
        listIpPlayer.clear();
        for (int i=0; i<ipPlayers.size(); i++) {
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
        System.out.println("-------------------------");
    }

    /*
    *   Setta l'ip del leader localmente ad ogni player e
    *   se sono il leader inizializzo Game, mescolo il mazzo
    *   e distribuisco le carte ai player a partire da quello
    *   alla mia destra (miaPos + 1) e
    *   setto il mio mazzo uguale al mazzo ritornato da testDistribution
    *   (mazzo senza 7 carte di i-esimo player)
    */
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

    /*
    *   Pusha 7 carte dal mazzo globale nel mio mazzo locale
    */
    public Game testDistribution (Game uno) {
        for (int i = 0; i<7; i++){
            MyCard.add(uno.mazzo.pop());
        }
        uno.MyCard = MyCard;
        mioController.setGame(uno);
        mioController.printMyDeck();
        return uno;
    }

}
