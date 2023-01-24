import Controller.Threads.ConnectivityThread;
import Model.User;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectivityThreadTest {


    private User user;
    private int testNum = 1;
    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the User unit");
    }

    @BeforeEach
    public void setup() throws UnknownHostException {
        System.out.println("***************** Test number : "+testNum+" ********************");
        user = new User( InetAddress.getLocalHost(),5010);
        ConnectivityThread.getInstance().setUser(user);
    }
    @Test
    void setUser() throws UnknownHostException {
        User user1 = new User(InetAddress.getLocalHost(),6530);
        user1.setPseudo("Omar");
        ConnectivityThread.getInstance().setUser(user1);
        Assertions.assertNotEquals("walid","Omar");
    }

    @Test
    void getInstance() {
        Assertions.assertEquals("walid",ConnectivityThread.getInstance().getUser().getPseudo());
    }

    @Test
    void setFlag() {
        ConnectivityThread.setFlag(true);
        Assertions.assertTrue(ConnectivityThread.isFlag());
    }

    @Test
    void isFlag() {
        Assertions.assertFalse(ConnectivityThread.isFlag());
    }

    @Test
    void deconnection() {
        Assertions.assertThrows(IOException.class,()->{
            ConnectivityThread.getInstance().deconnection();
        });
    }

    @Test
    void getUser() {
    }
    @AfterEach
    public void afterEachTest(){
        testNum++;
        User.getActive_agents().clear();
    }
    @AfterAll
    public void AfterAllTests(){
        System.out.println("Tests finished");
    }
}