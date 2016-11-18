package ca.ualberta.ridr;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class DriverTest {
    @Test
    public void testDriverEquality() {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle);
        assertTrue(driver.equals(driver));
    }

    @Test
    public void testDriverAcceptRequest(){
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Request request = new Request("campus", "home");

        driver.acceptRequest(request);

        assertEquals(driver.getRequests().size(), 1);
        assertTrue(driver.getRequests().get(0).equals(request));
    }
}
