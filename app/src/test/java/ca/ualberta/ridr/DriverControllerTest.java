package ca.ualberta.ridr;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by kristynewbury on 2016-11-07.
 */

public class DriverControllerTest {

    @Test
    public void testDriverControllerAcceptRequest(){
        Request request = new Request("campus", "home");
        Vehicle vehicle = new Vehicle(1800, "Carriage", "firstEver");
        Driver driver = new Driver("joe", new Date(), "credit", "email", "phone", vehicle, "bankno");

        DriverController DC = new DriverController();

        DC.acceptRequest(driver, request);

        assertEquals(driver.getRequests().size(), 1);
        assertTrue(driver.getRequests().get(0).equals(request));

    }
}
