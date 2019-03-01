import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    public ClientFunctions() throws RemoteException {
        super();
    }
    public void getIp(String ipPlayer) {
        System.out.println("Ho ricevuto ip: "+ipPlayer);
        listIpPlayer.add(ipPlayer);
        System.out.println("Ho pushato ip: "+ipPlayer);
    }

}