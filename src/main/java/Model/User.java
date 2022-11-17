package Model;

import java.net.DatagramSocket;
import java.net.InetAddress;
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

    public boolean choose_pseudo(String pseudo){
        return true;
    }

    public void identify_active_agents(){

    }

    public void pseudo_selected(String pseudo){

    }

    public boolean modify_pseudo(String pseudo){
        return true;
    }

    public void update_agents_list(String pseudo){

    }

    public void add_user(User u){
        active_agents.add(u);
    }

    public String getPseudo() {
        return pseudo;
    }

}
