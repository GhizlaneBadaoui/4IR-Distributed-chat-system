package Controller;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;

public class Broadcast extends Protocols {
    //the total number of agents that are connected to the system which gonna used by a user after broadcasting the sytem.
    private int total;
    public static Broadcast broadcast;

    static {
        try {
            broadcast = new Broadcast(Broadcast.get_broadcast_address(), 5600);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private Broadcast(InetAddress Ip, int port){
        this.IP = Ip;
        this.port = port;
        total = 0;
    }

    private boolean broadcatsting(String pseudo){
        return true;
    }

    public static InetAddress get_broadcast_address() throws UnknownHostException, SocketException {
        Iterator<InetAddress> networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getInetAddresses().asIterator();
        InetAddress brodct = null;
        while (networkInterface.hasNext()){
            brodct = networkInterface.next();
        }
        return brodct;
    }







}
