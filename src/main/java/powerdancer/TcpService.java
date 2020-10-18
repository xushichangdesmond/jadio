package powerdancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TcpService implements MulticastReceiver.Listener, Runnable {

    final List<Socket> clients = new CopyOnWriteArrayList<>();
    byte[] buffer;

    @Override
    public void onInit(byte[] buffer, int length) {
        System.out.println("onInit");
        this.buffer = buffer;
        onPacket(length);
    }

    @Override
    public void onPacket(int length) {
//        System.out.println("onPacket");
        clients.forEach(c->{
            try {
                c.getOutputStream().write(length>>8);
                c.getOutputStream().write(length&0XFF);
                c.getOutputStream().write(buffer, 0, length);
            } catch(Exception e) {
                try {
                    c.close();
                } catch(Exception ex) {}
                clients.remove(c);
            }
        });
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(6789);
            while(true) {
                try {
                    clients.add(ss.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        TcpService s = new TcpService();
        new Thread(s).start();
        new MulticastReceiver(s, null).run();
    }
}
