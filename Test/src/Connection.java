import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Connection extends UnicastRemoteObject implements ConnectionInterface {
    private int hostMax;
    public boolean max = false;
    private int random;
    public static ArrayList<String> listIp = new ArrayList<>();
    public Connection(int numHostMax) throws RemoteException {
        super();
        this.hostMax = numHostMax;
    }
    public int connect(String ipPlayer) {
        System.out.println("sei dentro connect");
        if (listIp.size() == hostMax) {
            // Non so se ci entrerà mai
            System.out.println("Numero massimo di player raggiunto");
        }
        else if (listIp.size() == (hostMax - 1)) {
            System.out.println("Sei l'ultimo Player!");
            listIp.add(ipPlayer);
            max = true;
            Random rand = new Random();
            this.random = rand.nextInt(listIp.size());
            System.out.println("il leader è in posizione "+random+", cioè è :"+listIp.get(random));
        }
        else {
            listIp.add(ipPlayer);
        }
        for(int i = 0; i < listIp.size(); i++){
            System.out.println("Player :"+i+" :"+listIp.get(i));
        }
        try {
            for (int i=0; i<listIp.size(); i++) {
                PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + listIp.get(i) + "/ciao");
                stub.getIp(listIp);
                System.out.println("Ho chiamato la getIp su Player: "+ listIp.get(i));
                // function ping in Server for start at the end.
                if (max) {
                    // stub.ping();
                    stub.electionLeader(listIp.get(random));
                }
            }
            if (max) {
                System.out.println("Chiusura Server For Start");
                return -1;
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        return 1;
    }
    public void kill() {
        System.exit(0);
    }

}
