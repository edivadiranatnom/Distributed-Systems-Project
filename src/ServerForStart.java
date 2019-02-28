import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerForStart implements connectionInterface{
    private static Utility utility = new Utility();
    static ArrayList<String> listIp = null;
    public ServerForStart() {
    }
    // findIp()
    public String connect(String ip) {
        System.out.println("sei dentro connect");
        listIp.add(ip);
        for(int i = 0; i < listIp.size(); i++){
            System.out.println(listIp.get(i));
        }
        return "Ti sei connesso: "+ip;
    }
    public static void main (String[] args) throws Exception{
        String ip = utility.findIp();
        System.out.println("Server For Start ip is: "+ip);
        try {
            ServerForStart obj = new ServerForStart();
            connectionInterface stub = (connectionInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            // Registry registry = LocateRegistry.getRegistry();
            // registry.bind("connection", stub);
            Naming.rebind("rmi://" + ip + "/connection", stub);


            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
