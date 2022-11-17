package Controller;

import Controller.Protocoles.Protocols;

import java.net.*;
import java.util.Iterator;

public class Broadcast extends Protocols {
    //the total number of agents that are connected to the system which gonna used by a user after broadcasting the sytem.
    private int total;

    private DatagramSocket connectivity_sock;
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

    private boolean broadcasting(String pseudo){
        return true;
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
