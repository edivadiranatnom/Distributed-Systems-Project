import java.rmi.Remote;

public interface ConnectionInterface extends Remote {
    void connect (String ip) throws Exception;
}
