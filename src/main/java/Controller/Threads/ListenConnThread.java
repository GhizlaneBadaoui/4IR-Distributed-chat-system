package Controller.Threads;

import Model.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ListenConnThread extends Thread {
    private ServerSocket user_listen_sock;
    private User user;

    private static Map<String,Socket> map_sockets;

    public ListenConnThread(User user) throws IOException {
        user_listen_sock = new ServerSocket(9000);
        this.user = user;
        this.start();
    }

    private String SearchAgent(InetAddress ip, int port){
        for (User user : this.user.getActive_agents()){
            if(user.getPort() == port && user.getIP().getHostAddress().equals(ip.getHostAddress()))
                return user.getPseudo();
        }
        return "";
    }

    public static Map<String, Socket> getMap_sockets() {
        return map_sockets;
    }

    @Override
    public void run() {
        Socket sock;
        String pseudo;
        while (true){
            try {
                sock = user_listen_sock.accept();
                if (sock != null){
                    pseudo = SearchAgent(sock.getInetAddress(),sock.getPort());
                    map_sockets.put(pseudo,sock);
                    if(!pseudo.equals(""))
                        (new ReceiverThread(sock,pseudo)).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
