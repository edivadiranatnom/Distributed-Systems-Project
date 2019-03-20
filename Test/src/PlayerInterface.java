import java.rmi.Remote;
import java.util.ArrayList;
import UnoGame.*;

public interface PlayerInterface extends Remote {
    void setIp (ArrayList<String> ip) throws Exception;
    void setLeader (String leader) throws Exception;
    int cardDistribution (ArrayList<Card> playersCards) throws Exception;
    void communicateTurno(Game uno) throws Exception;
    void preStartGame(Game uno) throws Exception;
}