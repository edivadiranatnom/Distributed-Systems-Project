import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Utility {
    public Utility() {}
    public static String findIp() throws Exception {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i instanceof Inet4Address && !i.getHostAddress().equals("127.0.0.1") /*&& (i.getHostAddress().contains("130.136") || i.getHostAddress().contains("192.168"))*/) {
                    return i.getHostAddress();
                }
            }
        }
        return "error";
    }
}
