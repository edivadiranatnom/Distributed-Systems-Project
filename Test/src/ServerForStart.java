import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerForStart{
    private static Utility utility = new Utility();
    public static Connection StartRMI;
    public ServerForStart() {
    }

    public static void main (String[] args) throws Exception{
        String ip = utility.findIp();
        System.out.println("Server For Start ip is: "+ip);
        System.setProperty("java.rmi.server.hostname", ip);
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            LocateRegistry.getRegistry(1099).list();
            System.out.println("rmiregistry already started");
        }
        try {
            //ServerForStart obj = new ServerForStart();
            // ConnectionInterface stub = (ConnectionInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            // Registry registry = LocateRegistry.getRegistry();
            // registry.bind("connection", stub);

            //
            StartRMI = new Connection();
            Naming.rebind("rmi://"+ip+"/connection", StartRMI);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        // broadcast
        // appena raggiungo il limite faccio questa chiamata su TUTTI i CLIENT.

    }
}
