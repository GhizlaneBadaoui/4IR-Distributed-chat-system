package Controller.Protocoles;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

public class Broadcast extends Protocols {

    private DatagramSocket connectivity_sock;
    private static Broadcast broadcast;
    private DatagramPacket packet;

    private DatagramSocket sock;
    private String msg = "broadcast pseudo = ";


    static {
        try {
            broadcast = new Broadcast(Broadcast.get_broadcast_address(), 5600);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private Broadcast(InetAddress Ip, int port) throws SocketException {
        this.IP = Ip;
        this.port = port;
        connectivity_sock = new DatagramSocket(port);
        connectivity_sock.setSoTimeout(5000);
        sock = new DatagramSocket();
        packet = new DatagramPacket(msg.getBytes(), msg.length(),broadcast.IP,5002);
    }

    public boolean broadcasting(String pseudo) {
        msg += pseudo;
        packet.setData(msg.getBytes());
        try {
            connectivity_sock.send(packet);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean send_msg(DatagramPacket pack){
        try {
            sock.send(pack);
        }
        catch (Exception e){
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
        Iterator<InetAddress> networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInetAddresses().asIterator();
        InetAddress brodct = null;
        while (networkInterface.hasNext()){
            brodct = networkInterface.next();
        }
        return brodct;
    }
}
