package Model;

import Controller.Protocoles.Broadcast;
import Controller.Threads.ConnectivityThread;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
    private String pseudo;
    private InetAddress IP;
    private int port;
    private String name;
    private ImageView imgSrc;
    private Integer incomingMsg;
    private static List<User> active_agents = new ArrayList<>();

    public User() {}

    public User(InetAddress IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public User(String pseudo, InetAddress IP, int port) {
        this.pseudo = pseudo;
        this.IP = IP;
        this.port = port;
    }

    public boolean choose_pseudo(String pseudo) throws IOException {
        if(Broadcast.getInstance().broadcasting(pseudo)){
            if(identify_active_agents()){
                this.pseudo = pseudo;
                this.pseudo_selected();//on fait passer l'utilisateur à l'interface principale de l'application
            }
            else
                return false;//l'utilisateur se connecte pas et un msg d'errur apparaitera.
        }
        return true;
    }

    public boolean identify_active_agents() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[1000],1000);
        String resp;
        while (true){
            try {
                System.out.println("okay");
                Broadcast.getInstance().getConnectivity_sock().receive(packet);
                resp = new String(packet.getData()).trim();
                System.out.println("msg rec : "+resp);
                if(!resp.contains("no") && resp.contains("ok")){
                    this.active_agents.add(new User(resp.substring(resp.indexOf(':')+1),packet.getAddress(), Integer.parseInt(resp.substring(4,resp.indexOf(':')))));
                    System.out.println("agents number = "+this.active_agents.size());
                }
                else{
                    System.out.println("in else bloc");
                    this.active_agents.clear();
                    return false;
                }
            }
            catch (SocketTimeoutException e){
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public void pseudo_selected() throws SocketException, UnknownHostException {
        Broadcast.getInstance().broadcasting(this.pseudo+"@port:"+this.port);
        ConnectivityThread.getInstance().setUser(this);
        if(ConnectivityThread.isFlag()){
            ConnectivityThread.getInstance().setUser(this);
            ConnectivityThread.setFlag(false);
        }
        else
            ConnectivityThread.getInstance().start();
    }

    public synchronized void delete_user(String pseudo){
        Iterator<User> iter = User.getActive_agents().stream().iterator();
        while (iter.hasNext()){
            if(iter.next().getPseudo().equals(pseudo)){
                User.getActive_agents().remove(iter);
                System.out.println("user deleted");
            }
        }
    }
    public boolean modify_pseudo(String pseudo) throws IOException {
        return choose_pseudo(pseudo);
    }

    public void update_agents_list(String pseudo){

    }
    public void inform_connected_agents(String msg){

    }

    public static List<User> getActive_agents() {
        return active_agents;
    }

    public void add_user(User u){
        active_agents.add(u);
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(ImageView imgSrc) {
        imgSrc.setFitHeight(60);
        imgSrc.setFitWidth(60);
        this.imgSrc = imgSrc;

    }

    public Integer getIncomingMsg() {
        return incomingMsg;
    }

    public void setIncomingMsg(Integer incomingMsg) {
        this.incomingMsg = incomingMsg;
    }
}