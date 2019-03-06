import java.rmi.Remote;

public interface ConnectionInterface extends Remote {
    int connect (String ip) throws Exception;
    void kill () throws  Exception;
}
