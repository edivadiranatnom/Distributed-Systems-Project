import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import UnoGame.*;


public class Player{
    private static Utility utility = new Utility();
    public  ClientFunctions Client;
    PlayerInterface stubPlayer;
    public static ArrayList<String> listIpPlayer = new ArrayList<>();

    public Player() {}

    public static void main (String[] args) {
    }

    Game startPlayer(String serverIp) throws Exception{
        // start Gui
        String ip = utility.findIp();
        System.out.println("il mio ip: "+ip);
        String host = serverIp;
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            LocateRegistry.getRegistry(1099).list();
            System.out.println("rmiregistry already started");
        }

        // server service
        try {
            Client = new ClientFunctions();
            Naming.rebind("rmi://"+ip+"/ciao", Client);

            System.err.println("Ready for Ips...");
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }

        // client service
        String responseIpLeader = null;
        try {
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://"+host+"/connection");
            responseIpLeader = stub.connect(ip);
            listIpPlayer.clear();
            listIpPlayer = Client.listIpPlayer;
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
        Game uno = new Game();
        uno.setLeader(responseIpLeader);
        return uno;
    }

    void killServerFor(String serverIp) throws Exception{
        ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://"+serverIp+"/connection");
        try {
            stub.kill();
        } catch (Exception e) {}
    }

    Game distribute (Game uno) throws Exception{
        String leader = uno.getLeader();
        if (utility.findIp().equals(leader)) {
            uno.mazzo.shuffle();
            int nPlayers = listIpPlayer.size();
            int myIndex = listIpPlayer.indexOf(leader);
            Game unoTmp = new Game();
            for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
                ArrayList<Card> playersCards = new ArrayList<>();
                for (int j = 0; j<7; j++){
                    playersCards.add(uno.mazzo.pop());
                }
                stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
                int ris = stubPlayer.cardDistribution(playersCards);
                if (ris!=1){
                    System.out.println("non torna 1\n");
                }
            }
            uno.pushScarti(uno.mazzo.pop());
            uno.giocatoreTurno = listIpPlayer.get(myIndex+1);

            for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
                stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
                stubPlayer.preStartGame(uno);
            }
        }
        return uno;
    }

    void getTurno(Game uno) throws Exception{
        int nPlayers = listIpPlayer.size();
        int myIndex = listIpPlayer.indexOf(uno.getLeader());
        for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
            PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i%nPlayers) + "/ciao");
            stubPlayer.communicateTurno(uno);
        }
    }
}
