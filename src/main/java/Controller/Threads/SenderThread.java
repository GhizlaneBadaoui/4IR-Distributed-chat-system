package Controller.Threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SenderThread extends Thread{
    private Socket sock;
    private String pseudo;

    private String msg;
    private ObjectOutputStream outputStream;


    public SenderThread(Socket sock, String pseudo, String msg) throws IOException {
        this.sock = sock;
        this.pseudo = pseudo;
        this.msg = msg;
        outputStream = new ObjectOutputStream(sock.getOutputStream());
    }
    @Override
    public void run() {
        while (sock.isConnected()) {
            try {
                outputStream.writeObject(msg);
                System.out.println("msg send to user");
                //il faut traiter le type de message - aussi ajouter le message dans la bdd
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
