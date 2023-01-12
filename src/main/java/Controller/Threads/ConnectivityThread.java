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
    private DatagramPacket packet_rec;
    private DatagramPacket packet_send;

    public ConnectivityThread(User user) throws UnknownHostException, SocketException {
        this.user = user;
        this.connectivity_sock = new DatagramSocket(5000);
        packet_rec = new DatagramPacket(new byte[1000],1000);
        packet_send = new DatagramPacket(new byte[1000],1000);
    }

    @Override
    public void run(){
        String data;
        while (true){
            try {
                connectivity_sock.receive(packet_rec);
                data = new String(packet_rec.getData()).trim();
                packet_send.setAddress(packet_rec.getAddress());
                packet_send.setPort(packet_rec.getPort());
                System.out.println("data = "+packet_rec.getData().length);
                if(!data.contains("port:") && data.contains("pseudo")) {
                    if (data.substring(9).equals(user.getPseudo())) {
                        System.out.println("msg rec from conn: "+data);
                        packet_send.setData("no".getBytes());
                        connectivity_sock.send(packet_send);
                    } else {
                        System.out.println("msg rec 2 : "+data);
                        packet_send.setData(("ok"+this.user.getPort()+":"+this.user.getPseudo()).getBytes());
                        connectivity_sock.send(packet_send);
                    }
                }
                if(data.contains("port:") && data.contains("pseudo")){
                    System.out.println(data.substring(data.indexOf("=")+2)+" index = "+data.indexOf("@"));
                    String pseu = data.substring(data.indexOf('=')+2,data.indexOf("@"));
                    user.add_user(new User(pseu,packet_rec.getAddress(),Integer.parseInt(data.substring(data.indexOf(':')+1))));
                    System.out.println("agents : "+User.getActive_agents().size());
                }
                packet_send.setData(new byte[1000]);
                packet_rec.setData(new byte[1000]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}