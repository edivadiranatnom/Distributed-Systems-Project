import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Connection extends UnicastRemoteObject implements ConnectionInterface {
    public static ArrayList<String> listIp = new ArrayList<>();
    public Connection() throws RemoteException {
        super();
    }
    public String connect(String ipPlayer) {
        System.out.println("sei dentro connect");
        listIp.add(ipPlayer);
        for(int i = 0; i < listIp.size(); i++){
            System.out.println("Player :"+i+" :"+listIp.get(i));
        }
        try {
            for (int i=0; i<listIp.size(); i++) {
                PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + listIp.get(i) + "/ciao");
                stub.getIp(listIp);
                System.out.println("Ho chiamato la getIp su Player: "+ listIp.get(i));
                stub.ping();
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        return "ti sei connesso e ti ho mandato gli ip";
    }

}
