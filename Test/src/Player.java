import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import UnoGame.*;

public class Player {
    private static Utility utility = new Utility();
    public ClientFunctions Client;
    PlayerInterface stubPlayer;
    public int portRegistry = 1099;
    public static ArrayList<String> listIpPlayer = new ArrayList<>();

    public Player() {
    }

    public static void main(String[] args) {
    }
    private void setRegistry() throws Exception {
        System.out.println("into setRegistry");
        try {
            LocateRegistry.createRegistry(portRegistry);
        }  catch (RemoteException e) {
            portRegistry = portRegistry + 1;
            System.out.println("rmiregistry already started. Try to set on port: " + portRegistry);
            setRegistry();
        }
    }

    Game startPlayer(String serverIp, GUIController gc) throws Exception {
        // start Gui
        String ip = utility.findIp();
        System.out.println("il mio ip: " + ip);
        String host = serverIp;
        setRegistry();
        System.out.println("la mia porta è: "+portRegistry);
        ip = ip + ":" + portRegistry;
        // server service
        try {
            Client = new ClientFunctions();
            Naming.rebind("rmi://" + ip + "/ciao", Client);

            System.err.println("Ready for Ips...");
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }

        // client service
        String responseIpLeader = null;
        try {
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://" + host + ":1099" +"/connection");
            responseIpLeader = stub.connect(ip);
            listIpPlayer.clear();
            listIpPlayer = Client.listIpPlayer;
            Client.mioController = gc;
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
        Game uno = new Game();
        uno.setLeader(responseIpLeader, portRegistry);
        return uno;
    }

    void killServerFor(String serverIp) throws Exception {
        ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://" + serverIp + "/connection");
        try {
            stub.kill();
        } catch (Exception e) {
        }
    }

    /**
     * entri solo se sei il leader
     * inutile controllare con il mio ip
     * poppa() localmente dal mazzo 7 carte alla volta, per ogni Client
     * lookup su ogni Client: cardDistribution passando le sue 7 carte.
     * Finito il giro ne parte un altro dove comunica a tutti:
     *  - il turno
     *  - il mazzo
     *  - lo scarto
     */

    Game distribute(Game uno) throws Exception {
        String leader = uno.getLeader();
        uno.mazzo.shuffle();
        int nPlayers = listIpPlayer.size();
        int myIndex = listIpPlayer.indexOf(leader);
        for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
            ArrayList<Card> playersCards = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                playersCards.add(uno.mazzo.pop());
            }
            stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
            stubPlayer.cardDistribution(playersCards);
        }
        uno.pushScarti(uno.mazzo.pop());
        uno.giocatoreTurno = listIpPlayer.get(myIndex + 1);

        for (int i = myIndex + 1; i < nPlayers + myIndex + 1; i++) {
            stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(i % nPlayers) + "/ciao");
            stubPlayer.preStartGame(uno);
        }
        return uno;
    }
}
