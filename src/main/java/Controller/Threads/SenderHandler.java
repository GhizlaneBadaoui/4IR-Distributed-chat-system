package Controller.Threads;

import Model.User;

import java.io.*;
import java.net.Socket;

public class SenderHandler {

    private Socket socket;
    private static SenderHandler senderHandler;

    static {senderHandler= new SenderHandler();}

    private SenderHandler(){}

    public Socket startConnection(String pseudo) {
        User usr = User.getUser(pseudo);
        System.out.println("pseudo to connect with"+pseudo);
        if(usr != null)
            try {
                Socket sock = new Socket(usr.getIP(),usr.getPort());
                ListenConnThread.getInstance().addNewSock(pseudo,sock);
                return sock;
            }
            catch (IOException e){
                return null;
            }
        else
            return null;
    }

    public boolean isEtablished(String pseudo){
        if(ListenConnThread.getInstance().getSock(pseudo).isConnected())
            return true;
        else
            return false;
    }

    public static SenderHandler getInstance() {
        return senderHandler;
    }
}
