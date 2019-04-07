import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class Ping extends TimerTask {
    private Timer timer;
    private GUIController mioController;
    private PlayerInterface stub;

    Ping(Timer timer, PlayerInterface stub, GUIController gc) {
        this.timer = timer;
        this.stub = stub;
        this.mioController = gc;
    }

    public void run() {
        String response = null;

        try {
            response = this.stub.ping();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Il giocatore si è disconnesso in PING");
            timer.cancel();
            timer.purge();
            mioController.bar();
        }

//        System.out.println("response: " + response);
    }
}