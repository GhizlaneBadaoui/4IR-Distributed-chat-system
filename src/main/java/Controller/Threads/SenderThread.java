package Controller.Threads;

import View.HomeInterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static Controller.Database.Operations.add;
import static Model.User.getUser;

public class SenderThread extends Thread{
    private Socket sock;
    private String pseudo;

    private String msg;
    private BufferedWriter bufferedWriter;


    public SenderThread(Socket socket, String pseudo, String msg) throws IOException {
        this.sock = socket;
        this.pseudo = pseudo;
        this.msg = msg;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }
    @Override
    public void run() {
            try {
                bufferedWriter.write(msg);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println("msg send to user");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                add(msg, dtf.format(LocalDateTime.now()), 'S', getUser(pseudo).getDbID());
                HomeInterface.currentHomeInter.refreshConversation(pseudo, getUser(pseudo).getDbID());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error sending message to the client");
        }
    }
}
