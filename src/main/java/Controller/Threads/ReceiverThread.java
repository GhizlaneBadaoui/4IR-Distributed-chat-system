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
    private BufferedWriter outputStream;
    private BufferedReader inputStream;

    public static List<ReceiverThread> receivers = new ArrayList<>();

    public ReceiverThread(Socket socket, String pseudo) throws IOException {
        try {
            this.sock = socket;
            this.pseudo = pseudo;
            outputStream = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
            inputStream = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error creating receiver thread.");
            e.printStackTrace();
        }
    }
    public void run() {
        while (sock.isConnected()){
            try {
                char[] data = new char[100];
                System.out.println("reciever thread -> to recieve the msgs from "+pseudo);
                inputStream.read(data);
                String msg = new String(data).trim();
                System.out.println(pseudo+"sended the following msg : "+msg);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                add(msg, dtf.format(LocalDateTime.now()), 'R', pseudo);
                HomeInterface.currentHomeInter.setConversationData();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error receiving message from the client");
                closeEverything(sock, inputStream);
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
