import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Connection extends UnicastRemoteObject implements ConnectionInterface {
    private int hostMax; // Giocatori che si connettono
    public boolean max = false;
    private int random;
    public static ArrayList<String> listIp = new ArrayList<>();

    public Connection(int numHostMax) throws RemoteException {
        super();
        this.hostMax = numHostMax;
    }

    public int connect(String ipPlayer) {
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
            System.out.println("Leader in posizione "+random+", cioè è: "+listIp.get(random));
        }

        //CASO k-esimo player k=[1,...,n-1]
        else {
            listIp.add(ipPlayer);
        }

        //
        try {
            for (int i=0; i<listIp.size(); i++) {
                PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + listIp.get(i) + "/ciao");
                stub.getIp(listIp);
                System.out.println("Ho chiamato la getIp su Player: "+ listIp.get(i));

                // entra solo n-esimo player che lancia l'elezione del leader
                if (max) {
                    // stub.ping();
                    stub.electionLeader(listIp.get(random));
                }
            }
            if (max) {
                System.out.println("Chiusura Server For Start");
                return 0;
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
