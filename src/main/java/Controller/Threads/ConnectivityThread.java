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
                if(data.contains("broadcast") && data.contains("pseudo")) {
                    if (data.substring(16).equals(user.getPseudo())) {
                        System.out.println("msg rec ");
                        packet.setData("no".getBytes());
                        connectivity_sock.send(packet);
                    } else {
                        System.out.println("msg rec 2");
                        packet.setData(("ok "+this.user.getPort()+":"+this.user.getPseudo()).getBytes());
                        connectivity_sock.send(packet);
                    }
                }
                if(!data.contains("broadcast") && data.contains("pseudo")){
                    user.add_user(new User(data.substring(data.indexOf(':')+1),packet.getAddress(),Integer.parseInt(data.substring(0,data.indexOf(':')-1))));
                    // smthing to add
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
