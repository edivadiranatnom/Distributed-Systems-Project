import UnoGame.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    private static Utility utility = new Utility();
    public Card[] MyCard = new Card[7];
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
        for (int i = 0; i<7; i++){
            MyCard[i] = new Card();
        }
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
        if ((utility.findIp()).equals(leader)){
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
            //
            int miaPos = listIpPlayer.indexOf(leader);
            int pos;
            if (miaPos == (listIpPlayer.size()-1)) {
                pos = 0;
            } else {
                pos = miaPos+1;
            }
            Game uno = new Game();
            Deck deck = uno.shuffle();
            System.out.println("sono il leader in posizione "+miaPos);
            PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://"+listIpPlayer.get(pos)+"/ciao");
            stubPlayer.testDistribution(deck);
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

    public void testDistribution (Deck deck) {
        System.out.println("Il mio mazzo é:\n");
        for (int i = 0; i<7; i++){
            MyCard[i] = deck.carddeck[i];
            System.out.println(MyCard[i].card+"\n"+MyCard[i].color);
        }
    }

}
