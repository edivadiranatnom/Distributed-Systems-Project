import UnoGame.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    private static Utility utility = new Utility();
    public ArrayList<Card> MyCard = new ArrayList<>();
    //public Card[] MyCard = new Card[7];
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
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
        this.leader = leader;
        int nPlayers = listIpPlayer.size();
        Game uno = new Game();
        Deck deck = uno.shuffle();
        Deck tmpDeck;
        if ((utility.findIp()).equals(leader)){
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
            // circular array
            int miaPos = listIpPlayer.indexOf(leader);
            for (int i = miaPos; i < nPlayers + miaPos; i++) {
                PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
                tmpDeck = stubPlayer.testDistribution(deck);
                deck = tmpDeck;
            }
        } else {
            System.out.println("il leader è: "+leader);
        }
    }

    // dopo morte server for start

    public void ringComunicaition(int a) throws Exception {
        String ip = utility.findIp();
        a=a+1;
        System.out.println(a);
        int miaPos = listIpPlayer.indexOf(ip);
        int pos = miaPos+1;
        // if (miaPos == (listIpPlayer.size()-1)) {
           // pos = 0;
        //} else {
         //   pos = miaPos+1;
        //}
        if (miaPos != (listIpPlayer.size()-1)) {
            PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(pos) + "/ciao");
            stubPlayer.ringComunicaition(a);
        }
    }

    public Deck testDistribution (Deck deck) {
        System.out.println("il deck era lungo: "+deck.carddeck.size());
        System.out.println("Il mio mazzo é:\n");
        for (int i = 0; i<7; i++){
            MyCard.add(deck.pop());
            System.out.println(MyCard.get(i).card+" "+MyCard.get(i).color+"\n");
        }
        System.out.println("ora deck è lungo: "+deck.carddeck.size());
        return deck;
    }

}
