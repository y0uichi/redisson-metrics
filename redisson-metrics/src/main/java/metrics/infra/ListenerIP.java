package metrics.infra;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ListenerIP {

    InetAddress address;

    public static ListenerIP INSTANCE = new ListenerIP();

    public ListenerIP() {

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress loopbackAddress = null;

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        address = addr;
                    } else {
                        loopbackAddress = addr;
                    }
                }
            }

            if (null == address) {
                address = loopbackAddress;
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public String toString() {
        if (null == address)
            return "unknown";
        return address.getHostAddress();
    }
}
