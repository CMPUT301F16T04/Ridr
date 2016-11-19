package ca.ualberta.ridr;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testRequestControllerGetPossibleDrivers(){
        Request request = new Request("Edmonton", "Timbuktu");
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        Vehicle vehicle = new Vehicle(1990, "pontiac", "grandam");
        Driver john = new Driver("john", new Date(), "credit", "email", "phone", vehicle, "bankaccountno");
        drivers.add(john);
        request.setPossibleDrivers(drivers);

        RequestController RC = new RequestController();

        ArrayList<Driver> testdrivers = RC.getPossibleDrivers(request);

        assertTrue(testdrivers.get(0).equals(john));
    }
    @Test
    public void testRequestControllerRemoveRequest(){
        RequestController RC = new RequestController();
        Request request = new Request("Edmonton", "Timbuktu");
        Rider rider = new Rider("Guy", new Date(), "credit", "email", "phone");

        rider.addRequest(request);

        //check that it was successfully added
        assertEquals(rider.getRequests().size(), 1);

        RC.removeRequest(request, rider);

        //then the real check is to make sure the controller can remove it
        assertEquals(rider.getRequests().size(), 0);

    }

}
