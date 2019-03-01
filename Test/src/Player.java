import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Player{
    private static Utility utility = new Utility();
    public static ClientFunctions Client;
    public Player() {}
    public static void main(String[] args) throws Exception{
        String ip = utility.findIp();
        System.out.println("il mio ip: "+ip);
        String host = (args.length < 1) ? null : args[0];
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
    }
}
