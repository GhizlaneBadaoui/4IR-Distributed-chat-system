package Controller.Threads;

import com.example.chatsystem.HomeInterface;

import java.io.*;
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
    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;
    public static List<ReceiverThread> receivers = new ArrayList<>();

    public ReceiverThread(Socket socket, String pseudo) throws IOException {
        try {
            this.sock = socket;
            this.pseudo = pseudo;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error creating receiver thread.");
            e.printStackTrace();
        }
    }
    public void run() {
        while (sock.isConnected()){
            try {
                System.out.println("reciever thread -> to recieve the msgs from "+pseudo);
                String msg = bufferedReader.readLine();
                System.out.println(pseudo+"sended the following msg : "+msg);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                add(msg, dtf.format(LocalDateTime.now()), 'R', pseudo);
                HomeInterface.currentHomeInter.setConversationData();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error receiving message from the client");
                //closeEverything(sock, bufferedReader);
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
