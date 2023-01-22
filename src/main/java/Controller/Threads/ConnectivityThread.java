package Controller.Threads;

import Controller.Database.Operations;
import Controller.Protocoles.Broadcast;
import Model.User;
import Controller.Interfaces.HomeInterface;

import java.io.IOException;
import java.net.*;

import static Controller.Database.Operations.addPseudo;
import static Controller.Database.Operations.modifyPseudo;

public class ConnectivityThread extends Thread{
    private static volatile boolean flag = false;
    private User user;
    private DatagramSocket connectivity_sock;
    private DatagramPacket packet_rec;
    private DatagramPacket packet_send;
    private static ConnectivityThread connectivityThread;

    static {
        try {
            connectivityThread = new ConnectivityThread();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    private ConnectivityThread() throws UnknownHostException, SocketException {
        this.connectivity_sock = new DatagramSocket(5000);
        packet_rec = new DatagramPacket(new byte[1000],1000);
        packet_send = new DatagramPacket(new byte[1000],1000);
    }
    public void setUser(User user) {
        this.user = user;
    }

    public static ConnectivityThread getInstance(){
        return connectivityThread;
    }

    public static void setFlag(boolean flag) {
        ConnectivityThread.flag = flag;
    }

    public static boolean isFlag() {
        return flag;
    }

    private void set_packet_info(){
        packet_send.setAddress(packet_rec.getAddress());
        packet_send.setPort(packet_rec.getPort());
    }

    public void deconnection(){
        Broadcast.getInstance().broadcasting(this.user.getPseudo()+"@deconnection@");
        this.user.deconnection();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run(){
        String data;
        while (true){
            while (flag);
            try {
                System.out.println("hello there from connectivity");
                connectivity_sock.receive(packet_rec);
                System.out.println("recivedSSS");
                if (!packet_rec.getAddress().equals(InetAddress.getLocalHost())) {
                    set_packet_info();
                    data = new String(packet_rec.getData()).trim();
                    System.out.println("data = " + packet_rec.getData().length);
                    if(data.contains("_#@") && data.contains("new")){
                        String oldps = data.substring(data.indexOf("=")+2, data.indexOf("@"));
                        String newps = data.substring(data.indexOf("new")+6);
                        modifyPseudo(1,newps);
                        User.AgentModifyPseudo(oldps,newps);
                        HomeInterface.currentHomeInter.refreshTable();
                    }
                    else if(data.contains("@@@!") && data.contains("pseudo")){
                            if (data.substring(data.indexOf("=")+1, data.indexOf("@")).equals(user.getPseudo()))
                                packet_send.setData("no".getBytes());
                            else
                                packet_send.setData("ok".getBytes());
                    }
                    else if (data.contains("@deconnection@") && data.contains("pseudo")) {
                            System.out.println("deconnection : pseudo = "+data.substring(9,data.indexOf("@")));
                            this.user.delete_user(data.substring(9, data.indexOf("@")));
                    }
                    else if (!data.contains("port:") && data.contains("pseudo")) {
                            if (data.substring(9).equals(user.getPseudo())) {
                                System.out.println("msg rec from conn: " + data);
                                packet_send.setData("no".getBytes());
                                connectivity_sock.send(packet_send);
                            } else {
                                System.out.println("msg rec 2 : " + data);
                                packet_send.setData(("ok" +this.user.getPort() + ":" + this.user.getPseudo()+"@id = "+this.user.getDbID()).getBytes());
                                connectivity_sock.send(packet_send);
                            }
                    }
                    else if (data.contains("port:") && data.contains("pseudo")) {
                            System.out.println(data.substring(data.indexOf("=") + 2) + " index = " + data.indexOf("@"));
                            String pseu = data.substring(data.indexOf('=') + 2, data.indexOf("@"));
                            int id = Integer.parseInt(data.substring(data.indexOf("@id@")+7));
                            user.add_user(new User(pseu, packet_rec.getAddress(), Integer.parseInt(data.substring(data.indexOf(':') + 1,data.indexOf('-'))),id));
                            if(!Operations.exist(id))
                                addPseudo(pseu,id);
                            System.out.println("agents : " + User.getActive_agents().size());
                    }
                    packet_send.setData(new byte[1000]);
                    packet_rec.setData(new byte[1000]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}