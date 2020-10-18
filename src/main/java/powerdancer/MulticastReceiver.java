package powerdancer;

import javax.sound.sampled.AudioSystem;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class MulticastReceiver extends Thread {

    final Listener listener;
    final String networkInterfaceName;

    public MulticastReceiver(Listener listener, String networkInterfaceName) {
        this.listener = listener;
        this.networkInterfaceName = networkInterfaceName;
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.printf("Display name: %s\n", netint.getDisplayName());
        System.out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }

    public interface Listener {

        default void onInit(byte[] buffer, int length) {
            onPacket(length);
        }

        void onPacket(int length);
    }

    public void run() {
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets))
                displayInterfaceInformation(netint);

            NetworkInterface networkInterface = getNetworkInterface(networkInterfaceName);

            System.out.println("mixer names-----");
            Arrays.stream(AudioSystem.getMixerInfo()).forEach(i -> System.out.println(i.getName()));

            MulticastSocket socket = new MulticastSocket(4010);
            InetAddress group = InetAddress.getByName("239.255.77.77");
            socket.joinGroup(new InetSocketAddress(group, 4010), networkInterface);

            byte[] buffer = new byte[100000];
            DatagramPacket packet = new DatagramPacket(buffer, 1157);
            socket.receive(packet);
            listener.onInit(buffer, packet.getLength());

            while (true) {
                packet = new DatagramPacket(buffer, 1157);
                socket.receive(packet);
                listener.onPacket(packet.getLength());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static NetworkInterface getNetworkInterface(String name) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.getName().equals(name)) {
                return netint;
            }
        }
        return null;
    }


}
