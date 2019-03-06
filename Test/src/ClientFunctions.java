import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientFunctions extends UnicastRemoteObject implements PlayerInterface {
    public ArrayList<String> listIpPlayer = new ArrayList<>();
    private static Utility utility = new Utility();
    public String leader;
    public boolean iamleader = false;

    public ClientFunctions() throws RemoteException {
        super();
    }
    public void getIp(ArrayList<String> ipPlayers) {
        listIpPlayer.clear();
        for (int i=0; i<ipPlayers.size(); i++) {
            listIpPlayer.add(ipPlayers.get(i));
            System.out.println("Ho pushato ip: " + ipPlayers.get(i));
        }
        System.out.println("-------------------------");
    }

    public void ping(){
        for (int i=0; i<listIpPlayer.size(); i++) {
            System.out.println("Ping: " + listIpPlayer.get(i)+"\n");
        }
        System.out.println("Ready to play");
    }
    public void electionLeader(String leader) throws Exception{
        this.leader = leader;
        if ((utility.findIp()).equals(leader)){
            System.out.println("io sono il leader: "+leader);
            this.iamleader = true;
        } else {
            System.out.println("il leader è: "+leader);
        }
    }

    // dopo morte server for start

    public void ringComunicaition(int a) throws Exception {
        String ip = utility.findIp();
        a=a+1;
        System.out.println(a);
        int miaPos = listIpPlayer.indexOf(ip);
        int pos = miaPos+1;
        // if (miaPos == (listIpPlayer.size()-1)) {
           // pos = 0;
        //} else {
         //   pos = miaPos+1;
        //}
        if (miaPos != (listIpPlayer.size()-1)) {
            PlayerInterface stubPlayer = (PlayerInterface) Naming.lookup("rmi://" + listIpPlayer.get(pos) + "/ciao");
            stubPlayer.ringComunicaition(a);
        }
    }

}
