import UnoGame.*;
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
    }

    /*
    *   Ad ogni nuova connessione, pulisce e aggiorna la lista
    *   degli ip di ogni player
    */
    public void getIp(ArrayList<String> ipPlayers) {
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
    public void electionLeader(String leader) throws Exception{
        String ip = utility.findIp();
        this.leader = leader;
        int nPlayers = listIpPlayer.size();
        if ((ip).equals(leader)){
            Game uno = new Game();
            Deck deck = new Deck();
            deck.shuffle();
            Deck tmpDeck;
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
            int miaPos = listIpPlayer.indexOf(leader);

            for (int i = miaPos + 1; i < nPlayers + miaPos + 1; i++) {
                PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
                tmpDeck = stubPlayer.testDistribution(deck, uno, ip);
                deck = tmpDeck;
            }
        } else {
            System.out.println("il leader è: "+leader);
        }
//        System.out.println("stampa del mio mazzo");
//        for (int i = 0; i<7; i++) {
//            System.out.println(MyCard.get(i).card + " " + MyCard.get(i).color + "\n");
//        }
    }

    /*
    *   Pusha 7 carte dal mazzo globale nel mio mazzo locale
    */
    public Deck testDistribution (Deck deck, Game uno, String ip) {

        for (int i = 0; i<7; i++){
            MyCard.add(deck.pop());
        }
        uno.AllPlayersCards.put(ip, MyCard);

        for (int i = 0; i<listIpPlayer.size(); i++) {
            uno.stampaCarte(listIpPlayer.get(i));
        }

        return deck;
    }

}
