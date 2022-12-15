package Controller.Threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiverThread extends Thread{
    private Socket sock;
    private String pseudo;
    private ObjectInputStream inputStream;

    public ReceiverThread(Socket sock, String pseudo) throws IOException {
        this.sock = sock;
        this.pseudo = pseudo;
        inputStream = new ObjectInputStream(sock.getInputStream());
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
