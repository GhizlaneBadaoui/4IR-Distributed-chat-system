package Controller.Threads;

import com.example.chatsystem.HomeInterface;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Controller.Database.Operations.add;

public class SenderThread extends Thread{
    private Socket sock;
    private String pseudo;

    private String msg;
    private OutputStreamWriter outputStream;


    public SenderThread(Socket sock, String pseudo, String msg) throws IOException {
        this.sock = sock;
        this.pseudo = pseudo;
        this.msg = msg;
        outputStream = new OutputStreamWriter(sock.getOutputStream());
    }
    @Override
    public void run() {
            try {
                outputStream.write(msg);
                System.out.println("msg send to user");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Date msgDate = Date.valueOf(dtf.format(LocalDateTime.now()));
                add(msg, msgDate, 'S', pseudo);
                HomeInterface.currentHomeInter.setConversationData();
                //il faut traiter le type de message - aussi ajouter le message dans la bdd
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
        }
    }
}
