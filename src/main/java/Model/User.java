package Model;

import Controller.Protocoles.Broadcast;

import java.sql.Connection;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
public class User {
    private String IdBdd;
    private String pseudo;
    private InetAddress IP;
    private int port;
    private String lastname;
    private String firstname;
    private List<User> active_agents;

    public User(String idBdd, String pseudo, InetAddress IP, int port, String lastname, String firstname, List<User> active_agents) {
        IdBdd = idBdd;
        this.pseudo = pseudo;
        this.IP = IP;
        this.port = port;
        this.lastname = lastname;
        this.firstname = firstname;
        this.active_agents = active_agents;
    }

    public User(String pseudo, InetAddress IP, int port) {
        this.pseudo = pseudo;
        this.IP = IP;
        this.port = port;
    }

    public boolean choose_pseudo(String pseudo) throws IOException {
        if(Broadcast.getInstance().broadcasting(pseudo)){
            if(identify_active_agents())
                this.pseudo_selected();//on fait passer l'utilisateur à l'interface principale de l'application
            else
                return false;//l'utilisateur se connecte pas et un msg d'errur apparaitera.
        }
        return false;
    }

    public boolean identify_active_agents() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[100],100);
        String resp;
        while (true){
            try {
                Broadcast.getInstance().getConnectivity_sock().receive(packet);
                resp = new String(packet.getData());
                if(!resp.contains("no") && resp.contains("ok")){
                    this.active_agents.add(new User("",resp.substring(resp.indexOf(':')+1),packet.getAddress(), Integer.getInteger(resp.substring(4,resp.indexOf(':'))),null,null,null));
                }
                else{
                    this.active_agents.clear();
                    return false;
                }
            }
            catch (Exception e){
                break;
            }
        }
        return true;
    }

    public void pseudo_selected(){
        String msg = this.port+":"+this.pseudo;
        for (User user : this.active_agents) {
            if(!Broadcast.getInstance().send_msg(new DatagramPacket(msg.getBytes(),msg.length(),user.IP,user.port)))
                System.out.println("A problem occurs while sending a UDP msg to : "+user.pseudo);
        }
    }

    public boolean modify_pseudo(String pseudo) throws IOException {
        return choose_pseudo(pseudo);
    }

    public void update_agents_list(String pseudo){

    }

    public void inform_connected_agents(String msg){

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
}
