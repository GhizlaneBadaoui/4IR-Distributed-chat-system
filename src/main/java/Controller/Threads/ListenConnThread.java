package Controller.Threads;

import Model.User;

import java.io.IOException;
import java.net.*;
import java.util.Map;

public class ListenConnThread extends Thread {
    private ServerSocket user_listen_sock;
    private static ListenConnThread listenConnThread;

    private static Map<String,Socket> map_sockets;

    static {
        try {
            listenConnThread = new ListenConnThread();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ListenConnThread getInstance() {
        return listenConnThread;
    }

    public ListenConnThread() throws IOException {
        user_listen_sock = new ServerSocket(9000);
    }

    private String SearchAgent(InetAddress ip, int port){
        for (User user : User.getActive_agents()){
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
                System.out.println("listen thread ahs been launched");
                sock = user_listen_sock.accept();
                System.out.println("an agent has been added");
                if (sock != null){
                    pseudo = SearchAgent(sock.getInetAddress(),sock.getPort());
                    System.out.println("pseudo from list = "+pseudo);
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
