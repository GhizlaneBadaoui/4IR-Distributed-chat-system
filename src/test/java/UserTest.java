import Model.User;
import org.junit.jupiter.api.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {
    User user;
    private int testNum = 1;

    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the User unit");
    }

    @BeforeEach
    public void setup() throws UnknownHostException {
        System.out.println("***************** Test number : "+testNum+" ********************");
        user = new User("walid",InetAddress.getLocalHost(),5010,1);
        User.getActive_agents().add(user);
    }

    @Test
    void setPort() throws UnknownHostException {
        user.setPort(5100);
        Assertions.assertEquals(5100,user.getPort());
    }

    @Test
    void delete_user() throws UnknownHostException {
        Assertions.assertEquals(1,User.getActive_agents().size());
        User.getActive_agents().remove(user);
        Assertions.assertEquals(0,User.getActive_agents().size());
    }

    @Test
    void getUser() throws UnknownHostException {
        Assertions.assertEquals(1,User.getActive_agents().size());
    }

    @Test
    void add_user() throws UnknownHostException {
        Assertions.assertEquals(user.getPseudo(),User.getActive_agents().get(0).getPseudo());
    }

    @Test
    void getPseudo() throws UnknownHostException {
        Assertions.assertEquals(user.getPseudo(),User.getActive_agents().get(0).getPseudo());
    }

    @Test
    void getPort() throws UnknownHostException {
        Assertions.assertEquals(user.getPort(),User.getActive_agents().get(0).getPort());
    }

    @Test
    void getIP() throws UnknownHostException {
        Assertions.assertEquals(user.getIP().getHostAddress(),User.getActive_agents().get(0).getIP().getHostAddress());
    }

    @Test
    void setPseudo() throws UnknownHostException {
        user.setPseudo("Mohammed");
        Assertions.assertEquals("Mohammed",user.getPseudo());
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