import UnoGame.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Player{
    private static Utility utility = new Utility();
    public static ClientFunctions Client;
    public static ArrayList<String> listIpPlayer = new ArrayList<>();

    public Player() {}

    public static void main (String[] args) {
    }

    int startPlayer(String serverIp) throws Exception{
        // start Gui
        String ip = utility.findIp();
        System.out.println("il mio ip: "+ip);
        String host = serverIp;
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
        int response = -1;
        try {
            // Registry registry = LocateRegistry.getRegistry(host);
            // ConnectionInterface stub = (ConnectionInterface) registry.lookup("connection");
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://"+host+"/connection");
            response = stub.connect(ip);
            if (response == 0) {
                listIpPlayer = Client.listIpPlayer;
                try {
                    stub.kill();
                } catch (Exception e) {
                    //System.out.println(e);
                }
            }
            //System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
        return response;
    }
}
