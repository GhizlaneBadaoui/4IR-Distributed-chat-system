package Controller.Threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SenderThread extends Thread{
    private Socket sock;
    private ObjectOutputStream outputStream;

    public SenderThread(Socket sock) throws IOException {
        this.sock = sock;
        outputStream = new ObjectOutputStream(sock.getOutputStream());
        this.start();
    }

    @Override
    public void run() {
        Object msg;
        while (true){
            try {
                outputStream.writeObject(new Object());
                //il faut traiter le type de message - aussi ajouter le message dans la bdd
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
