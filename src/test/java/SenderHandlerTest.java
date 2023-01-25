import Controller.Threads.ConnectivityThread;
import Controller.Threads.ListenConnThread;
import Controller.Threads.SenderHandler;
import org.junit.jupiter.api.*;

import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SenderHandlerTest {

    private int testNum = 1;
    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the SenderHandler unit");
    }

    @BeforeEach
    public void setup() throws UnknownHostException {
        System.out.println("***************** Test number : "+testNum+" ********************");
        ListenConnThread.getInstance().getMap_sockets().put("walid",new Socket());
    }
    @Test
    void isEtablished() {
        Assertions.assertTrue(SenderHandler.getInstance().isEtablished("walid"));
    }
    @AfterEach
    public void afterEachTest(){
        testNum++;
        ListenConnThread.getInstance().getMap_sockets().clear();
    }
    @AfterAll
    public void AfterAllTests(){
        System.out.println("Tests finished");
    }
}