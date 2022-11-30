package Controller.Threads;

import Model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectivityThread extends Thread{
    private User user;
    private DatagramSocket connectivity_sock;
    private DatagramPacket packet;
    private byte[] msg = new byte[100];

    public ConnectivityThread(DatagramSocket connectivity_sock, User user) throws UnknownHostException {
        this.user = user;
        this.connectivity_sock = connectivity_sock;
        packet = new DatagramPacket(msg,msg.length, null,5002);
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
                        packet.setData("no".getBytes());
                        connectivity_sock.send(packet);
                    } else {
                        packet.setData(("ok "+this.user.getPort()+":"+this.user.getPseudo()).getBytes());
                        connectivity_sock.send(packet);
                    }
                }
                if(!data.contains("broadcast") && data.contains("pseudo")){
                    user.add_user(new User("",data.substring(data.indexOf(':')+1),packet.getAddress(),Integer.parseInt(data.substring(0,data.indexOf(':')-1)),"","",null));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
