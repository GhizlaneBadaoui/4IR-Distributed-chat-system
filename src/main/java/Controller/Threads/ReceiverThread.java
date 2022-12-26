package Controller.Threads;

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
    public void run() {
        while (sock.isConnected()){
            try {
                Object msg = bufferedReader.readLine();
                // ajouter le message dans la bdd
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error receiving message to the client");
                closeEverything(sock, bufferedReader);
                break;
            }
        }
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
