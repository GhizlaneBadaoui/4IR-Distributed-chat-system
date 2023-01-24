import Controller.Protocoles.Broadcast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;

class BroadcastTest {

    @Test
    void broadcasting() throws UnknownHostException {
        Assertions.assertTrue(Broadcast.getInstance().broadcasting("walid"));
    }
}