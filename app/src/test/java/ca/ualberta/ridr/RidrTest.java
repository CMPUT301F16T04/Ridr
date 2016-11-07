package ca.ualberta.ridr;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mackenzie on 12/10/16.
 */
public class RidrTest{
    // Test for retrieving current ridr's open requests US 01.02.01*/
    private static final String TAG = "MyActivity";

    @Test
    public void testGetOpenRequests(){
        //NOTE: Does not use request MVC
        //Initialize classes for the test
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        //Create controller for rider
        RiderController riderController = new RiderController(rider);
        //Associate requests with their rider
        Request req1 = new Request(rider,"University of Alberta", " West Edmonton Mall");
        Request req2 = new Request(rider, "Rogers Place", "Whyte Ave");
        req2.setAccepted(true);

        riderController.addRequest(req1);
        riderController.addRequest(req2);

        //Check that the openRequests list is of the right size, and contains the correct item
        ArrayList<Request> openRequests = riderController.getOpenRequests();

        assertTrue(openRequests.size() == 1);
    }
<<<<<<< HEAD
=======

    @Test
    /*Driver accept requests offline, accepted once online for US 08.04.01 */
    public void offlineAcceptRequestTest() {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Ride ride = rider.createRide();

        driver.goOffline();
        if(driver.isOffline()) {
            driver.acceptRide(ride);
        }

        driver.goOnline();
        if(!driver.isOffline()) {
            assertTrue(driver.completeRide(ride));
            assertTrue(driver.isPayed());
        }
    }

    @Test
    /*Rider specify start and end on map for request for US 10.01.01 */
    public void RiderSetLocationMapTest() {
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");
        ArrayList<Request> requests = rider.getRequests();
        Map map = new Map();
        map.setStartPinLocation("University of Alberta");
        map.setEndPinLocation("West Edmonton Mall");

        assertEquals(requests.get(0).getPickup(), "University of Alberta");
        assertEquals(requests.get(0).getDropoff(), "West Edmonton Mall");
        assertEquals(map.getStartPinLocation(),requests.get(0).getPickup());
        assertEquals(map.getEndPinLocation(),requests.get(0).getDropoff());
    }

    @Test
    /* Driver view start and end geo locations on map for US 10.02.01*/
    public void driverLocationMapTest() {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        driver.addRide();

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");
        ArrayList<Request> requests = rider.getRequests();
        Map map = new Map();
        map.setStartPinLocation("University of Alberta");
        map.setEndPinLocation("West Edmonton Mall");

        assertEquals(requests.get(0).getPickup(), "University of Alberta");
        assertEquals(requests.get(0).getDropoff(), "West Edmonton Mall");
        assertEquals(map.getStartPinLocation(),requests.get(0).getPickup());
        assertEquals(map.getEndPinLocation(),requests.get(0).getDropoff());
    }


>>>>>>> design
}
