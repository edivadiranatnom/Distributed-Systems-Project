import java.rmi.Remote;

public interface PlayerInterface extends Remote {
    void getIp (String ip) throws Exception;
}