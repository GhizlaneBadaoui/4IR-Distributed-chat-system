package Controller.Threads;

import Model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ConnectivityThread extends Thread{
    private User user;
    private DatagramSocket connectivity_sock;
    private DatagramPacket packet;
    private byte[] msg = new byte[100];

    public ConnectivityThread(User user) throws UnknownHostException, SocketException {
        this.user = user;
        this.connectivity_sock = new DatagramSocket(5000);
        packet = new DatagramPacket(msg,msg.length);
    }

    @Override
    public void run(){
        String data;
        while (true){
            try {
                connectivity_sock.receive(packet);
                data = new String(packet.getData());
                if(!data.contains("port:") && data.contains("pseudo")) {
                    if (data.substring(9).equals(user.getPseudo())) {
                        System.out.println("msg rec ");
                        packet.setData("no".getBytes());
                        connectivity_sock.send(packet);
                    } else {
                        System.out.println("msg rec 2");
                        packet.setData(("ok"+this.user.getPort()+":"+this.user.getPseudo()).getBytes());
                        connectivity_sock.send(packet);
                    }
                }
                if(data.contains("port:") && data.contains("pseudo")){
                    String pseu = data.substring(data.indexOf('=')+2,data.indexOf('p'));
                    user.add_user(new User(pseu,packet.getAddress(),Integer.parseInt(data.substring(data.indexOf(':')+1))));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}