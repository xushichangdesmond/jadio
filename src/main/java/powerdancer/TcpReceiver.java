package powerdancer;

import java.io.InputStream;
import java.net.Socket;

public class TcpReceiver implements Runnable {
    final String targetIp;
    final int targetPort;

    public TcpReceiver(String targetIp, int targetPort) {
        this.targetIp = targetIp;
        this.targetPort = targetPort;
    }

    @Override
    public void run() {
        Player p = new Player();
        try {
            Socket s = new Socket(targetIp, targetPort);
            System.out.println("connected");
            byte[] buffer = new byte[10000];

            try (InputStream is = s.getInputStream()) {
                int len = (is.read() << 8) + is.read();
                System.out.println(len);
                p.onInit(buffer, is.read(buffer, 0, len));
//                System.out.println("connected");
                while (true) {
                    len = (is.read() << 8) + is.read();
                    is.read(buffer, 0, len);
                    p.onPacket(len);
//                    System.out.println("connected");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpReceiver(
                args.length > 0 ? args[0]: "127.0.0.1",
                args.length > 1 ? Integer.decode(args[1]): 6789
        ).run();
    }
}
