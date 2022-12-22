package Controller.Threads;

import java.io.IOException;

public class MessageThread extends Thread{

    private String pseudo;
    private String msg;

    public MessageThread(String pseudo, String msg) {
        this.pseudo = pseudo;
        this.msg = msg;
    }

    @Override
    public void run() {
        if(ListenConnThread.getMap_sockets().containsKey(this.pseudo)){
            try {
                (new SenderThread(ListenConnThread.getMap_sockets().get(pseudo),this.pseudo,msg)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
