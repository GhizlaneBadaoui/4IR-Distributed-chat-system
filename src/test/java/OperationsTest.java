import org.junit.jupiter.api.*;

import static Controller.Database.Operations.closeConnection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationsTest {

    private int testNum = 1;

    @BeforeAll
    public void BeforeAllTests(){
        System.out.println("We are testing the DB Operations unit");
    }

    @BeforeEach
    void setUp() {
        System.out.println("***************** Test number : "+testNum+" ********************");
    }

    @Test
    void displayMessagesWithAgent() {
    }

    @Test
    void exist() {
    }

    @Test
    void modifyPseudo() {
    }

    @Test
    void add() {
    }

    @Test
    void addPseudo() {
    }

    @Test
    void deleteAgent() {
    }

    @AfterEach
    public void afterEachTest(){
        testNum++;
        closeConnection();
    }

    @AfterAll
    public void AfterAllTests(){
        System.out.println("Tests finished");
    }
}