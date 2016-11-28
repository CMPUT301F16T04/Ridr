package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by kristynewbury on 2016-11-07.
 */

public class RequestControllerTest{
/*
    @Test
    public void testRequestControllerAccept(){
        LatLng coords = new LatLng(1,2);
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        RequestController RC = new RequestController();

        RC.accept(request);

        assertTrue(request.isAccepted());

    }
/*
    @Test
    public void testRequestControllerAddDriver() {
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        Driver driver = new Driver("joe", new Date(), "credit", "email", "phone", "bankno");

        RequestController RC = new RequestController();

        RC.addDriver(request, driver);

        assertEquals(request.getPossibleDrivers().size(), 1);
        assertTrue(request.getPossibleDrivers().get(0).equals(driver));
    }
    */

    /*@Test
    public void testRequestControllerGetPossibleDrivers(){
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        Driver john = new Driver("john", new Date(), "credit", "email", "phone", "bankaccountno");
        drivers.add(john);
        request.setPossibleDrivers(drivers);

        RequestController RC = new RequestController();

        ArrayList<Driver> testdrivers = RC.getPossibleDrivers(request);

        assertTrue(testdrivers.get(0).equals(john));
    }
    @Test
    public void testRequestControllerRemoveRequest(){
        RequestController RC = new RequestController();
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        rider.addRequest(request);

        //check that it was successfully added
        assertEquals(rider.getRequests().size(), 1);

        RC.removeRequest(request, rider);

        //then the real check is to make sure the controller can remove it
        assertEquals(rider.getRequests().size(), 0);

    }*/

}
