package Controller.Threads;

import com.example.chatsystem.HomeInterface;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Controller.Database.Operations.add;

public class SenderThread extends Thread{
    private Socket sock;
    private String pseudo;

    private String msg;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;


    public SenderThread(Socket sock, String pseudo, String msg) throws IOException {
        this.sock = sock;
        this.pseudo = pseudo;
        this.msg = msg;
        outputStream = new BufferedWriter(new OutputStreamWriter(this.sock.getOutputStream()));
        inputStream = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
    }
    @Override
    public void run() {
            try {
                outputStream.write(msg);
                System.out.println("msg send to user");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                add(msg, dtf.format(LocalDateTime.now()), 'S', pseudo);
                HomeInterface.currentHomeInter.setConversationData();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
        }
    }
}
