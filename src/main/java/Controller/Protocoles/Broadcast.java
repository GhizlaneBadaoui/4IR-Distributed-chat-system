package Controller.Protocoles;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

public class Broadcast {
    private InetAddress IP;
    private int port;
    private DatagramSocket connectivity_sock;
    private static Broadcast broadcast;
    private DatagramPacket packet;

    private String msg = "pseudo = ";


    static {
        try {
            broadcast = new Broadcast(Broadcast.get_broadcast_address(), 5600);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private Broadcast(InetAddress Ip, int port) throws SocketException, UnknownHostException {
        this.IP = Ip;
        this.port = port;
        connectivity_sock = new DatagramSocket(port);
        
        connectivity_sock.setSoTimeout(2000);
        packet = new DatagramPacket(msg.getBytes(), msg.length(),Ip,5000);
    }

    public boolean broadcasting(String _msg) {
        packet.setData((msg+""+_msg).getBytes());
        System.out.println("from brodcast : msg = "+new String(packet.getData()));
        try {
            connectivity_sock.send(packet);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public DatagramSocket getConnectivity_sock() {
        return connectivity_sock;
    }

    public static Broadcast getInstance(){
        return broadcast;
    }

    private static InetAddress get_broadcast_address() throws UnknownHostException, SocketException {
        Iterator<InterfaceAddress> networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInterfaceAddresses().iterator();
        return networkInterface.next().getBroadcast();
    }
}
