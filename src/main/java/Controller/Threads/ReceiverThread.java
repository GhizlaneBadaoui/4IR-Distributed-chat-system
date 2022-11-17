package Controller.Threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiverThread extends Thread{
    private Socket sock;
    private ObjectInputStream inputStream;

    public ReceiverThread(Socket sock) throws IOException {
        this.sock = sock;
        inputStream = new ObjectInputStream(sock.getInputStream());
        this.start();
    }

    @Override
    public void run() {
        Object msg;
        while (true){
            try {
                msg = inputStream.readObject();
                //il faut traiter le type de message - aussi ajouter le message dans la bdd
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
