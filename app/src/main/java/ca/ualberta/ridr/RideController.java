package ca.ualberta.ridr;

import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    RideController(){}


    public void createRide(Driver driver, Request request, Rider rider) {
        Ride ride = new Ride(driver, rider,  request.getPickup(), request.getDropoff(), new Date());
        rider.confirmDriver(ride);
    }
}
