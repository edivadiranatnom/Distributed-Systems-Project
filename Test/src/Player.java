import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Player{
    private static Utility utility = new Utility();
    public static ClientFunctions Client;
    public static int paramToDelete = 0;
    public static ArrayList<String> listIpPlayer = new ArrayList<>();
    private static boolean ReadyForPlay = false;
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
            int response = stub.connect(ip);
            if (response == -1) {
                //ReadyForPlay = true;
                listIpPlayer = Client.listIpPlayer;
                try {
                    stub.kill();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            // Comunicazione ad anello
            if (ReadyForPlay) {
                int miaPos = Client.listIpPlayer.indexOf(ip);
                int pos;
                if (miaPos == (Client.listIpPlayer.size()-1)) {
                    pos = 0;
                } else {
                    pos = miaPos+1;
                }
                System.out.println("sono in posizione "+miaPos+". Devo richiamare il client in pos: "+(pos));
                PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + Client.listIpPlayer.get(pos) + "/ciao");
                stubPlayer.ringComunicaition(paramToDelete);
            }
            //System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Player exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
