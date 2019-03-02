import java.rmi.Remote;
import java.util.ArrayList;

public interface PlayerInterface extends Remote {
    void getIp (ArrayList<String> ip) throws Exception;
}