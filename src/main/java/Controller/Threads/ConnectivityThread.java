package Controller.Threads;

import Model.User;
import com.example.chatsystem.HomeInterface;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.*;

public class ConnectivityThread extends Thread{
    private User user;
    private DatagramSocket connectivity_sock;
    private DatagramPacket packet;
    private byte[] msg = new byte[100];

    public ConnectivityThread() throws UnknownHostException, SocketException {
        this.user = user;
        this.connectivity_sock = new DatagramSocket(5000);
        packet = new DatagramPacket(msg,msg.length);
        this.start();
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
                    HomeInterface.vBoxMap.put(data.substring(data.indexOf(':')+1),new VBox());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        ConnectivityThread connectivityThread = new ConnectivityThread();
    }
}
