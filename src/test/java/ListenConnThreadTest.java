import Model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

class ListenConnThreadTest {

    private int testNum;
    User user;

    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the User unit");
    }

    @BeforeEach
    public void setup() throws UnknownHostException {
        System.out.println("***************** Test number : "+testNum+" ********************");
        user = new User("walid", InetAddress.getLocalHost(),5010,1);
        User.getActive_agents().add(user);
    }
    @Test
    void setMapPseudo() {
    }

    @Test
    void getInstance() {
    }

    @Test
    void addNewSock() {
    }

    @Test
    void getSock() {
    }

    @Test
    void getMap_sockets() {
    }
}