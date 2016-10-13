package ca.ualberta.ridr;

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
    /**General testing of classes */
    @Test
    public void createRideTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        User user = new User("Steve", new Date(), "321");
        assertTrue(new Ride(new Date(), driver, user, false  ) instanceof Ride);
    }

    @Test
    public void createDriverTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        assertTrue(new Driver("Jeff", new Date(), vehicle, "123") instanceof Driver);
    }

    @Test
    public void createUserTest() throws Exception{
        assertTrue(new User("Steve", new Date(), "321") instanceof  User);
    }

    @Test
    public void createVehicleTest() throws Exception{
        assertTrue(new Vehicle(1994, "chevy", "truck") instanceof Vehicle);
    }

    //Testing User Stories 1-6
    // Test for creating ride request US 01.01.01*/

    @Test
    public void testRequestCreation(){
        User user = new User("Steve", new Date(), "321");
        user.requestRide("University of Alberta", "West Edmonton Mall");
        ArrayList<Request> requests = user.getRequests();

        assertEquals(requests.get(0).getPickup(), "University of Alberta");
        assertEquals(requests.get(0).getDropoff(), "West Edmonton Mall");
    }

    // Test for retrieving current ridr's request US 01.02.01*/
    public void testgetRequests(){
        User user = new User("Steve", new Date(), "321");
        user.requestRide("University of Alberta", "West Edmonton Mall");
        ArrayList<Request> requests = user.getRequests();

        assertTrue(user.getRequests() instanceof Collection);
        assertTrue(user.getRequests().get(0) instanceof Request);
    }



    //**Testing of User stories 13-18/

    /** Test for geolocation US 04.01.01*/
    @Test
    public void geoLocationTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        //Add open ride to driver
        driver.addRide();

        //Sort the driver's arraylist of rides based on location for display
        String myLocation = "100.1.0.0, 192.168.1.0";
        assertTrue(driver.geoSort("100.0.0, 100.0.0", myLocation));
    }

    @Test
    /** Test for keyword search US 04.02.01 */
    public void keywordRideSearchTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        //Add open ride to driver
        driver.addRide();

        //Search driver's list of rides for the keyword
        assertTrue(driver.kewordSearch("James"));

    }

    @Test
    //** Test for driver request accept US 05.01.01*/
    public void driverAccceptRequestTest() throws Exception {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        User user = new User("Steve", new Date(), "321");

        Ride ride = user.createRide();

        driver.acceptRide(ride);
        assertTrue(driver.completeRide(ride));
        assertTrue(driver.isPayed());
    }

    @Test
    //** Test for view list of rides that have abeen accepted and are pending US 05.02.01*//
    public void driverRideStateTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        User user = new User("Steve", new Date(), "321");

        Ride ride = user.createRide();
        user.acceptRideOffer(driver);
        driver.acceptRide(ride);
        driver.completeRide(ride);
        Ride ride2 = user.createRide();
        driver.acceptRide(ride2);

        assertTrue(driver.getPendingRides());
        assertTrue(driver.getCompletedRides());
    }

    @Test
    //** Check ride state for driver US 05.03.01 */
    public void driverUserAcceptRideStateTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        User user = new User("Steve", new Date(), "321");

        Ride ride = user.createRide();
        user.acceptRideOffer(driver);

        assertTrue(driver.userAcceptedRide(ride));
    }

    @Test
    /** test push notification to Driver 05.04.01 */
    public void userAcceptPushTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), vehicle, "123");
        User user = new User("Steve", new Date(), "321");

        Ride ride = user.createRide();
        user.acceptRideOffer(driver);
        assertTrue(ride.pushAcceptedByUser());

    }

}
