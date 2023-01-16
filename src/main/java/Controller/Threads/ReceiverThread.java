package Controller.Threads;

import com.example.chatsystem.HomeInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Controller.Database.Operations.add;

public class ReceiverThread extends Thread{
    private Socket sock;
    private String pseudo;
    private ObjectInputStream bufferedReader;
    public static List<ReceiverThread> receivers = new ArrayList<>();

    public ReceiverThread(Socket socket, String pseudo){
        try {
            this.sock = socket;
            this.pseudo = pseudo;
            this.bufferedReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creating receiver thread.");
            e.printStackTrace();
        }
    }
    public void run() {
        while (sock.isConnected()){
            try {
                System.out.println("reciever thread -> to recieve the msgs from "+pseudo);
                String msg = String.valueOf(bufferedReader.readObject());
                System.out.println(pseudo+"sended the following msg : "+msg);
                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Date msgDate = Date.valueOf(dtf.format(LocalDateTime.now()));
                add((String) msg, Date.valueOf(dtf.format(LocalDateTime.now())), 'R', pseudo);
                HomeInterface.currentHomeInter.setConversationData();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error receiving message to the client");
                closeEverything(sock, bufferedReader);
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeEverything (Socket socket, ObjectInputStream bufferedReader) {
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
