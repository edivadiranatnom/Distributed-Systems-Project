import java.rmi.Remote;

public interface ConnectionInterface extends Remote {
    String connect (String ip) throws Exception;
}
