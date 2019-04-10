import java.rmi.Naming;
import java.util.Timer;
import java.util.TimerTask;

public class PingServer extends TimerTask {
    private Connection conn;

    PingServer(Connection c) {
        this.conn = c;
    }

    public void run() {
        for (int i =0; i<conn.listIp.size(); i++){
            try {
                PlayerInterface stub = (PlayerInterface) Naming.lookup("rmi://" + conn.listIp.get(i) + "/ciao");
                stub.ping();
            }catch(Exception e){
                conn.handleDisconnect(conn.listIp.get(i));
            }
        }
    }
}