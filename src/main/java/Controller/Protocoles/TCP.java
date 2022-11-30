package Controller.Protocoles;

import Controller.Protocoles.Protocols;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class TCP extends Protocols {
    ServerSocket listen_client_sock;

    public TCP(InetAddress ip, int port) throws IOException {
        this.IP = ip;
        this.port = port;
        listen_client_sock = new ServerSocket(5000);
    }

    public void wait_connection(){
        //lance un thread d'attente de Connexion depuis un autre agent
    }

    public void etablish_connection(InetAddress address){
        // Socket agent_sock;
    }






}
