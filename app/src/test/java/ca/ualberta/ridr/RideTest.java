package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class RideTest {
    @Test
    public void testRideEquality() {
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle);
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        LatLng coords = new LatLng(0,0);

        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date(),new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067));
        assertTrue(ride.equals(ride));
    }
}
