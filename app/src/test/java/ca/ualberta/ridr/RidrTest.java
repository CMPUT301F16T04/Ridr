package ca.ualberta.ridr;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

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

    //**Testing of User stories 13-18/

    


}
