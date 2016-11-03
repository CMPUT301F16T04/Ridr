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
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        assertTrue(new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date()) instanceof Ride);
    }

    @Test
    public void createDriverTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        assertTrue(new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123") instanceof Driver);
    }

    @Test
    public void createRiderTest() throws Exception{
        assertTrue(new Rider("Steve", new Date(), "321", "goodemail", "9999999") instanceof  Rider);
    }

    @Test
    public void createVehicleTest() throws Exception{
        assertTrue(new Vehicle(1994, "chevy", "truck") instanceof Vehicle);
    }

    //Testing Rider Stories 1-6
    // Test for creating ride request US 01.01.01*/

    @Test
    public void testRequestCreation(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");
        ArrayList<Request> requests = rider.getRequests();

        assertEquals(requests.get(0).getPickup(), "University of Alberta");
        assertEquals(requests.get(0).getDropoff(), "West Edmonton Mall");
    }

    // Test for retrieving current ridr's open requests US 01.02.01*/
    @Test
    public void testGetOpenRequests(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        Request request1 = new Request(rider, "University of Alberta", " West Edmonton Mall");
        Request request2 = new Request(rider,  "Rogers Place", "Whyte Ave");
        request1.setAccepted(false);
        request2.setAccepted(true);

        // Check only unaccepted ride is open
        assertEquals(rider.getOpenRequests().size(), 1);
        // Check unaccepted ride is request 1
        assertTrue(rider.getOpenRequests().get(0) == request1);


        assertTrue(rider.getOpenRequests() instanceof Collection);
        assertTrue(rider.getOpenRequests().get(0) != null);
    }

    // Test for retrieving current ridr's open requests US 01.02.01*/
    @Test
    public void testGetRequests(){

    }

    // Test for retrieving current ridr's request US 01.03.01*/
    @Test
    public void testAcceptedNotification(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        rider.getRequests().get(0).addAccepted(driver);

        ArrayList<Request> requests = rider.getRequests();

        assertTrue(requests.get(0).isAccepted());
        assertTrue(requests.get(0).getPossibleDrivers().size() > 0);
    }

    // Test for retrieving current ridr's request US 01.04.01*/
    @Test
    public void testCancelRequest(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");

        Request currentRequest = rider.getRequests().get(0);

        assertTrue(rider.getRequests().get(0).equals(currentRequest));
        rider.removeRequest(currentRequest);
        assertFalse(rider.getRequests().get(0).equals(currentRequest));

    }

    // Test for retrieving drivr's contact info US 01.05.01*/
    @Test
    public void testDrivrInfo(){
        String email = "driver@email.com";
        String phoneNumber = "555-555-5555";
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        // Sanity check
        assertTrue(driver instanceof Driver);


        assertEquals(driver.getEmail(), email);
        assertEquals(driver.getPhoneNumber(), phoneNumber);
    }

    // Test for retrieving ride's fair US 01.06.01*/
    @Test
    public void testFairEstimation(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");

        String email = "driver@email.com";
        String phoneNumber = "555-555-5555";
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date());
        ride.complete();

        Double fare = ride.getFare();

        assertTrue(ride.getCompleted());
        assertTrue(fare == 5.0);

    }


    // Testing for stories 7-12

    // Test for Ride Completion Confirmation US 01.07.01
    @Test
    public void testCompletionConfirmation(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        String email = "driver@email.com";
        String phoneNumber = "555-555-5555";
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date());

        assertFalse(ride.getCompleted());

        ride.complete();

        assertTrue(ride.getCompleted());
    }

    // Test for Confirm Driver US 01.08.01
    @Test
    public void testConfirmDriver(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        String email = "driver@email.com";
        String phoneNumber = "555-555-5555";
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        String emailT = "driver2@email.com";
        String phoneNumberT = "555-555-5565";
        Vehicle vehicleT = new Vehicle(1996, "chevy", "truck");
        Driver driverTwo = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date());
        driver.acceptRide(ride);
        driverTwo.acceptRide(ride);

        assertFalse(ride.hasDriver(driver));
        assertFalse(ride.hasDriver(driverTwo));
        rider.confirmDriver(driver);
        assertTrue(ride.hasDriver(driver));
        assertFalse(ride.hasDriver(driverTwo));

    }

    // Test for Request Status US 02.01.01
    @Test
    public void testRequestStatus(){
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        String email = "driver@email.com";
        String phoneNumber = "555-555-5555";
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");

        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date());

        assertFalse(ride.getCompleted());
        assertEquals(ride.getDriver(), driver);
        assertEquals(ride.getRider(), rider);
    }

    // Test for Rider Profile US 03.01.01
    @Test
    public void testRiderProfile(){
        Date date = new Date();
        Rider Rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        assertEquals("Steve", Rider.getName());
        assertEquals(date, Rider.getDateOfBirth());
        assertEquals("321", Rider.getCreditCard());
    }

    // Test for Edit Profile US 03.02.01
    @Test
    public void test(){
        Date date = new Date();
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        assertEquals("Steve", rider.getName());
        assertEquals(date, rider.getDateOfBirth());
        assertEquals("321", rider.getCreditCard());
        rider.setCreditCard("222");
        date = new Date();
        rider.setDateOfBirth(date);
        assertEquals(date, rider.getDateOfBirth());
        assertEquals("222", rider.getCreditCard());
    }

    //Test for Show Contact Information US 03.03.01
    @Test
    public void testShowContactInformation(){
        Date date = new Date();
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        assertEquals("Steve", rider.getName());
        assertEquals(date, rider.getDateOfBirth());
        assertEquals("321", rider.getCreditCard());
    }

    //**Testing of Rider stories 13-18/

    /** Test for geolocation US 04.01.01*/
    @Test
    public void geoLocationTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
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
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        //Add open ride to driver
        driver.addRide();

        //Search driver's list of rides for the keyword
        assertTrue(driver.kewordSearch("James"));

    }

    @Test
    //** Test for driver request accept US 05.01.01*/
    public void driverAccceptRequestTest() throws Exception {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        Ride ride = rider.createRide();

        driver.acceptRide(ride);
        assertTrue(driver.completeRide(ride));
        assertTrue(driver.isPayed());
    }

    @Test
    //** Test for view list of rides that have abeen accepted and are pending US 05.02.01*//
    public void driverRideStateTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        Ride ride = rider.createRide();
        rider.acceptRideOffer(driver);
        driver.acceptRide(ride);
        driver.completeRide(ride);
        Ride ride2 = rider.createRide();
        driver.acceptRide(ride2);

        assertTrue(driver.getPendingRides());
        assertTrue(driver.getCompletedRides());
    }

    @Test
    //** Check ride state for driver US 05.03.01 */
    public void driverRiderAcceptRideStateTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        Ride ride = rider.createRide();
        rider.acceptRideOffer(driver);

        assertTrue(driver.riderAcceptedRide(ride));
    }

    @Test
    /** test push notification to Driver 05.04.01 */
    public void RiderAcceptPushTest() throws Exception{
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider Rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        Ride ride = Rider.createRide();
        Rider.acceptRideOffer(driver);
        assertTrue(ride.pushAcceptedByRider());

    }

    @Test
    /* See requests while offline as Driver for US 08.01.01 */
    public void offlineDriverRequestListTest() throws Exception {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        rider.requestRide("University of Alberta", "West Edmonton Mall");
        driver.goOffline();
        if(driver.isOffline()) {
            ArrayList<Ride> rides = driver.getRides();
            assertTrue(rides instanceof Collection);
            assertTrue(rides.get(0) instanceof Ride);
        }
    }

    @Test
    /* Rider see requests while offline for US 08.02.01*/
    public void offlineRiderRequestsListTest() throws Exception {
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.requestRide("University of Alberta", "West Edmonton Mall");
        rider.goOffline();
        if(rider.isOffline()) {
            ArrayList<Request> requests = rider.getRequests();
            assertTrue(requests.size() > 0);
            assertTrue(requests instanceof Collection);
            assertTrue(requests.get(0) instanceof Request);
        }
    }

    @Test
    /* Rider able to send requests while offline, sent when online for US 08.03.01*/
    public void offlineSendRequestTest() throws Exception {
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        rider.goOffline();
        if(rider.isOffline()) {
            rider.requestRide("University of Alberta", "West Edmonton Mall");
        }
        rider.goOnline();
        if(!rider.isOffline()) {
            ArrayList<Request> requests = rider.getRequests();
            assertTrue(requests.get(0).isSent());
            assertTrue(requests.size() > 0);
            assertEquals(requests.get(0).getPickup(), "University of Alberta");
            assertEquals(requests.get(0).getDropoff(), "West Edmonton Mall");
        }
    }

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

}
