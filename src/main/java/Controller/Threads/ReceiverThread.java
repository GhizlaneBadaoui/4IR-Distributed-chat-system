package Controller.Threads;

import com.example.chatsystem.HomeInterface;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiverThread extends Thread{
    private Socket sock;
    private String pseudo;
    private BufferedReader bufferedReader;

    public ReceiverThread(Socket socket, String pseudo){
        try {
            this.sock = socket;
            this.pseudo = pseudo;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error creating receiver thread.");
            e.printStackTrace();
        }
    }

    public void receiveMessage(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sock.isConnected()){
                    try {
                        Object msg = bufferedReader.readLine();
                        HomeInterface.addLabel(msg, vBox);
                        //il faut traiter le type de message - aussi ajouter le message dans la bdd
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message to the client");
                        closeEverything(sock, bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything (Socket socket, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
