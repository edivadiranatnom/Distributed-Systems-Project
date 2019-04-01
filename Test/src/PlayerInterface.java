import java.rmi.Remote;
import java.util.ArrayList;
import UnoGame.*;

public interface PlayerInterface extends Remote {
    void setIp(ArrayList<String> ip) throws Exception;

    void setLeader(String leader, String myIp) throws Exception;

    void cardDistribution(ArrayList<Card> playersCards) throws Exception;

    void preStartGame(Game uno) throws Exception;

    void communicationCard(Game uno,Card cartagiocata) throws Exception;

    void removeDrawedCard(Card card, String ip) throws Exception;
}