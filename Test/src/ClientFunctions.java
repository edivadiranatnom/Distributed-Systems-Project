import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
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

    public void ping(){
        for (int i=0; i<listIpPlayer.size(); i++) {
            System.out.println("Ping: " + listIpPlayer.get(i)+"\n");
        }
        System.out.println("Ready to play");
    }

}
