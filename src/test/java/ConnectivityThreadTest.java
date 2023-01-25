import Controller.Threads.ConnectivityThread;
import Model.User;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectivityThreadTest {
    private User user;
    private int testNum = 1;
    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the ConnectivityThread unit");
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
        user.setPseudo("walid");
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
        Assertions.assertDoesNotThrow(()->{
            ConnectivityThread.getInstance().deconnection();
        });
    }

    @Test
    void getUser() {
        Assertions.assertEquals(user,ConnectivityThread.getInstance().getUser());
    }
    @AfterEach
    public void afterEachTest(){
        testNum++;
    }
    @AfterAll
    public void AfterAllTests(){
        System.out.println("Tests finished");
    }
}