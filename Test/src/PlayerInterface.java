import java.rmi.Remote;
import java.util.ArrayList;
import UnoGame.*;

public interface PlayerInterface extends Remote {
    void setIp (ArrayList<String> ip) throws Exception;
    void setLeader (String leader) throws Exception;
    Game testDistribution(Game uno) throws Exception;
}