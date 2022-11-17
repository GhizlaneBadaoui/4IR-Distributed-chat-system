package Controller.Threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenConnThread extends Thread {
    private ServerSocket user_listen_sock;

    public ListenConnThread(ServerSocket user_listen_sock) {
        user_listen_sock = user_listen_sock;
        this.start();
    }

    @Override
    public void run() {
        Socket sock;
        while (true){
            try {
                sock = user_listen_sock.accept();
                if (sock != null){
                    //to be continued
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
