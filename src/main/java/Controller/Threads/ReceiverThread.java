package Controller.Threads;

import View.HomeInterface;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Controller.Database.Operations.add;
import static Model.User.getUser;

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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public static void setPseudo(String oldps, String newps){
        Iterator<ReceiverThread> iter = receivers.stream().iterator();
        while (iter.hasNext()){
            ReceiverThread rt = iter.next();
            if(rt.getPseudo().equals(oldps)){
                rt.setPseudo(newps);
            }
        }
    }
    public void run() {
        while (sock.isConnected()){
            try {
                System.out.println("reciever thread -> to recieve the msgs from "+pseudo);
                String msg = bufferedReader.readLine();
                System.out.println(pseudo+"sended the following msg : "+msg);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                add(msg, dtf.format(LocalDateTime.now()), 'R', getUser(pseudo).getDbID());
                HomeInterface.currentHomeInter.refreshConversation(pseudo, getUser(pseudo).getDbID());
            } catch (IOException e) {
                System.out.println("TCP connection with an agent is closed");
                break;
            }
        }
    }
}
