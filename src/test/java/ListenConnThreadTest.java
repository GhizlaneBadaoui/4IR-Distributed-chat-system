import Controller.Threads.ConnectivityThread;
import Controller.Threads.ListenConnThread;
import Model.User;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListenConnThreadTest {

    private int testNum = 1;
    private ListenConnThread instance = ListenConnThread.getInstance();
    private User user = new User("walid",InetAddress.getLocalHost(),5010,1);

    ListenConnThreadTest() throws UnknownHostException {
    }

    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the ListenConnThread unit");
    }

    @BeforeEach
    public void setup() throws UnknownHostException {
        System.out.println("***************** Test number : "+testNum+" ********************");
        ConnectivityThread.getInstance().setUser(user);
    }
    @Test
    void setMapPseudo() {
        instance.getMap_sockets().put("walid",new Socket());
        instance.setMapPseudo("walid","Mohammed");
        Assertions.assertNotEquals("walid",instance.getMap_sockets().keySet().stream().iterator().next());
    }

    @Test
    void getInstance() {
        Assertions.assertEquals("walid", ConnectivityThread.getInstance().getUser().getPseudo());
    }

    @Test
    void addNewSock() {
        instance.addNewSock("Mohammed",new Socket());
        Assertions.assertTrue(instance.getMap_sockets().containsKey("Mohammed"));
    }

    @Test
    void getSock() throws IOException {
        Socket socket = new Socket();
        instance.addNewSock("Mohammed",new Socket());
        Assertions.assertEquals(socket.getPort(),instance.getSock("Mohammed").getPort());
    }

    @Test
    void getMap_sockets() {
        Assertions.assertEquals(instance.getMap_sockets().size(),ListenConnThread.getInstance().getMap_sockets().size());
    }

    @AfterEach
    public void afterEachTest(){
        testNum++;
        instance.getMap_sockets().clear();
    }
    @AfterAll
    public void AfterAllTests(){
        System.out.println("Tests finished");
    }

}