import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    public ClientFunctions() throws RemoteException {
        super();
    }
    public void getIp(ArrayList<String> ipPlayers) {
        for (int i=0; i<ipPlayers.size(); i++) {
            System.out.println("Ho ricevuto ip: " + ipPlayers.get(i));
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
    }

}
