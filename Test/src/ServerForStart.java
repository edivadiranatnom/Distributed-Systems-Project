import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import static java.lang.Integer.parseInt;

public class ServerForStart{
    private static Utility utility = new Utility();
    public static Connection StartRMI;
    public ServerForStart() {
    }

    public static void main (String[] args) throws Exception{
        // TODO: Try and catch --> Number Format Exception.
        int numHostMax = parseInt(args[0]);
        if (numHostMax < 2 || numHostMax > 8) {
            System.out.println("Numero massimo di giocatori: 8\n Numero minimo di giocatori: 3");
            System.exit(1);
        }
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
            StartRMI = new Connection(numHostMax);
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
