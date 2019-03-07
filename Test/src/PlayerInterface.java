import java.rmi.Remote;
import java.util.ArrayList;
import UnoGame.*;

public interface PlayerInterface extends Remote {
    void getIp (ArrayList<String> ip) throws Exception;
    void electionLeader (String leader) throws Exception;
    Deck testDistribution(Deck deck) throws Exception;
}