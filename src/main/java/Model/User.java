package Model;

import Controller.Protocoles.Broadcast;
import Controller.Threads.ConnectivityThread;
import Controller.Threads.ListenConnThread;
import Controller.View.HomeInterface;
import javafx.scene.image.Image;
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
    private String fullName;
    private ImageView imgSrc = new ImageView(new Image("file:src/main/resources/Images/person.png"));
    private static List<User> active_agents = new ArrayList<>();

    public User() {}

    public User(InetAddress IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public User(String pseudo, InetAddress IP, int port) {
        this.pseudo = pseudo;
        this.IP = IP;
        this.setImgSrc(new ImageView(new Image("file:src/main/resources/Images/person.png")));
        this.port = port;
    }

    public boolean choose_pseudo(String pseudo) throws IOException {
        if(Broadcast.getInstance().broadcasting(pseudo)){
            if(identify_active_agents()){
                this.pseudo = pseudo;
                this.pseudo_selected("");//on fait passer l'utilisateur à l'interface principale de l'application
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
                    System.out.println("port numer = "+Integer.parseInt(resp.substring(2,resp.indexOf(':'))));
                    this.active_agents.add(new User(resp.substring(resp.indexOf(':')+1),packet.getAddress(), Integer.parseInt(resp.substring(2,resp.indexOf(':')))));
                    System.out.println("port number 2 = "+active_agents.get(0).getPort());
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

    public void setPort(int port) {
        this.port = port;
    }

    public void pseudo_selected(String msg) throws IOException {
        Broadcast.getInstance().broadcasting(this.pseudo+"@port:"+this.port+msg);
        ConnectivityThread.getInstance().setUser(this);
        if(ConnectivityThread.isFlag()){
            ConnectivityThread.getInstance().setUser(this);
            ConnectivityThread.setFlag(false);
        }
        else {
            ConnectivityThread.getInstance().start();
            ListenConnThread.getInstance().start();
        }
    }

    public synchronized void delete_user(String pseudo){
        Iterator<User> iter = User.getActive_agents().stream().iterator();
        while (iter.hasNext()){
            User user = iter.next();
            if(user.getPseudo().equals(pseudo)){
                User.getActive_agents().remove(user);
                System.out.println("user deleted");
                HomeInterface.currentHomeInter.refreshTable();
                if (HomeInterface.currentHomeInter.getAgentPseudo().equals(pseudo)){
                    HomeInterface.currentHomeInter.restrictConversation();
                }
            }
        }
    }
    public boolean modifyPseudo(String pseudo) throws IOException {
        ConnectivityThread.setFlag(true);
        if(Broadcast.getInstance().broadcasting(pseudo+"@@@!")){
            if(isPseudoChange()){
                pseudo_selected("_#@-new = "+pseudo);
                this.pseudo = pseudo;
            }
            else
                return false;
        }
        return true;
    }

    private boolean isPseudoChange() {
        DatagramPacket packet = new DatagramPacket(new byte[1000],1000);
        while (true) {
            try {
                Broadcast.getInstance().getConnectivity_sock().receive(packet);
            } catch (SocketTimeoutException e) {
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String resp = new String(packet.getData()).trim();
            System.out.println("from pseudo change -> msg rec " + resp);
            if (resp.contains("no") && !resp.contains("ok"))
                return false;
        }
    }
    public void update_agents_list(String pseudo){

    }
    public void inform_connected_agents(String msg){

    }

    public static synchronized User getUser(String pseudo){
        Iterator<User> iter = User.getActive_agents().stream().iterator();
        while (iter.hasNext()){
            User user = iter.next();
            if(user.getPseudo().equals(pseudo)){
                return user;
            }
        }
        return null;
    }
    public void deconnection(){
        active_agents.clear();
        System.out.println("all users are deleted : "+active_agents.size());
    }

    public static List<User> getActive_agents() {
        return active_agents;
    }

    public void add_user(User u){
        active_agents.add(u);
        HomeInterface.currentHomeInter.refreshTable();
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

    public ImageView getImgSrc(int x) {
        imgSrc.setFitWidth(x);
        imgSrc.setFitHeight(x);
        return imgSrc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImgSrc(ImageView imgSrc) {
        imgSrc.setFitHeight(60);
        imgSrc.setFitWidth(60);
        this.imgSrc = imgSrc;
    }
}