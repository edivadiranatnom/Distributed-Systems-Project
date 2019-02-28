import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Player implements PlayerInterface{
    private static Utility utility = new Utility();
    public ArrayList<String> listIpPlayer = new ArrayList<>();

    private Player() {}
    public void getIp(String ipPlayer) {
        System.out.println("Ho ricevuto ip: "+ipPlayer);
        listIpPlayer.add(ipPlayer);
        System.out.println("Ho pushato ip: "+ipPlayer);
    }
    public static void main(String[] args) throws Exception{
        String ip = utility.findIp();
        System.out.println("il mio ip: "+ip);
        String host = (args.length < 1) ? null : args[0];
        // client service
        try {
            // Registry registry = LocateRegistry.getRegistry(host);
            // ConnectionInterface stub = (ConnectionInterface) registry.lookup("connection");
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://"+host+"/connection");
            String response = stub.connect(ip);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }

        // server service
        try {
            Player obj = new Player();
            PlayerInterface stub = (PlayerInterface) UnicastRemoteObject.exportObject(obj, 0);

            Naming.rebind("rmi://"+ip+"/getAllIp", stub);

            System.err.println("Ready for Ips...");
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
