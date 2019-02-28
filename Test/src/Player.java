import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Player {
    private static Utility utility = new Utility();

    private Player() {}
    public static void main(String[] args) throws Exception{
        String ip = utility.findIp();
        System.out.println("il mio ip: "+ip);
        String host = (args.length < 1) ? null : args[0];
        try {
            // Registry registry = LocateRegistry.getRegistry(host);
            // ConnectionInterface stub = (ConnectionInterface) registry.lookup("connection");
            ConnectionInterface stub = (ConnectionInterface) Naming.lookup("rmi://"+host+"/connection");
            String response = stub.connect(ip);
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
