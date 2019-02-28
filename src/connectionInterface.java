import java.rmi.Remote;

public interface connectionInterface extends Remote {
    String connect (String ip) throws Exception;
}
