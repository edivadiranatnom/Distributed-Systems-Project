import java.rmi.Remote;
import java.util.ArrayList;
import UnoGame.*;

public interface PlayerInterface extends Remote {
    void getIp (ArrayList<String> ip) throws Exception;
    void ringComunicaition(int param) throws Exception;
    void electionLeader (String leader) throws Exception;
    void testDistribution(Deck deck) throws Exception;
}