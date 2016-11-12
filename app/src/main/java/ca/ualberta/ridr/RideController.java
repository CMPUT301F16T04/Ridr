package ca.ualberta.ridr;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    RideController(){}


    public void createRide(Driver driver, Request request, Rider rider) {
        Ride ride = new Ride(driver, rider,  request.getPickup(), request.getDropoff(), new Date() , request.getPickupCoords(), request.getDropOffCoords());
        rider.confirmDriver(ride);

        //TODO ok currently makes new ride with each button press, not great? so fix , but at least it stores sturr

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride",ride.getId().toString(), rideString);
        System.out.println(s);

    }
}
