package ca.ualberta.ridr;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by kristynewbury on 2016-11-07.
 */

public class RequestControllerTest{

    @Test
    public void testRequestControllerAccept(){
        Request request = new Request("campus", "home");
        RequestController RC = new RequestController();

        RC.accept(request);

        assertTrue(request.isAccepted());

    }

    @Test
    public void testRequestControllerAddDriver() {
        Request request = new Request("campus", "home");
        Vehicle vehicle = new Vehicle(1800, "Carriage", "firstEver");
        Driver driver = new Driver("joe", new Date(), "credit", "email", "phone", vehicle, "bankno");

        RequestController RC = new RequestController();

        RC.addDriver(request, driver);

        assertEquals(request.getPossibleDrivers().size(), 1);
        assertTrue(request.getPossibleDrivers().get(0).equals(driver));
    }
}
