package ca.ualberta.ridr;

/**
 * Created by Justin on 2016-11-03.
 */
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class RiderTest {
    @Test
    public void TestRiderEquality() {
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        assertTrue(rider.equals(rider));
    }
}
