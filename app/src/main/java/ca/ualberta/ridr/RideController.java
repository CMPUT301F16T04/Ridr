package ca.ualberta.ridr;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    RideController(){}


    public void createRide(String driverName, Request request, String riderName) {
        //will need to replace the date I guess with actual date that ride is supposed to occur
        Ride ride = new Ride(driverName, riderName,  request.getPickup(), request.getDropoff(), request.getDate() , request.getPickupCoords(), request.getDropOffCoords());

        System.out.println(ride);
        //rider.confirmDriver(ride);
        //another cant do while the ride array list of rider is null

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride",ride.getId().toString(), rideString);

    }
}
