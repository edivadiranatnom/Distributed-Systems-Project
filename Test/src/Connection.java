import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import UnoGame.*;

public class Connection extends UnicastRemoteObject implements ConnectionInterface {
    private int hostMax; // Giocatori che si connettono
    public boolean max = false;
    private int random;
    public Game uno;
    public static ArrayList<String> listIp = new ArrayList<>();
    public Utility utility = new Utility();
    String leader = null;

    public Connection(int numHostMax) throws RemoteException {
        super();
        this.hostMax = numHostMax;
        Timer pingTimer = new Timer();
        pingTimer.schedule(new PingServer(this), 0, 1000);
    }

    public String connect(String ipPlayer) {
        System.out.println("sei dentro connect");
        //CASO n+1-esimo player
        if (listIp.size() == hostMax) {
            // Non so se ci entrerà mai
            System.out.println("Numero massimo di player raggiunto");
        }

        //CASO n-esimo player
        else if (listIp.size() == (hostMax - 1)) {
            System.out.println("Sei l'ultimo Player!");
            listIp.add(ipPlayer);
            max = true;
            Random rand = new Random();
            this.random = rand.nextInt(listIp.size());
            //leader = listIp.get(random);
        }

        //CASO k-esimo player k=[1,...,n-1]
        else {
            listIp.add(ipPlayer);
        }

        //
        try {
            leader = listIp.get(0);
            System.out.println("Leader in posizione "+0+", cioè è: "+leader);

            for (int i=0; i<listIp.size(); i++) {
                PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + listIp.get(i) + "/ciao");
                stub.setIp(listIp);
                System.out.println("Ho chiamato la getIp su Player: "+ listIp.get(i));
                //Il leader è sempre il primo e affamok
                stub.setLeader(listIp.get(0), listIp.get(i));
//                // entra solo n-esimo player che lancia l'elezione del leader
//                if (max) {
//                    //stub.setLeader(leader);
//                }
            }
            if (max) {
                System.out.println("Chiusura Server For Start");
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        return leader;
    }
    public void kill() {
        System.exit(0);
    }

    public void handleDisconnect(String ip){
        listIp.remove(ip);
        if(listIp.size()>0){
            leader = listIp.get(0);
            for (int i=0; i<listIp.size(); i++) {
                try {
                    PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + listIp.get(i) + "/ciao");
                    stub.setIp(listIp);
                    System.out.println("Ho chiamato la getIp su Player: " + listIp.get(i));
                    stub.setLeader(leader, listIp.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
