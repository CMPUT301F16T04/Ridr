package ca.ualberta.ridr;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    RideController(){}


    public void createRide(String driverId, Request request, String riderId) {
        Ride ride = new Ride(driverId, riderId,  request.getPickup(), request.getDropoff(), new Date() , request.getPickupCoords(), request.getDropOffCoords());

        //the idea here is to set the ride id to equal the request id, then if we have
        // multiple button presses to create a ride, we actually only create the one ride not many
        ride.setId(request.getID().toString());
        //either add fromreqId or boolean to accept button to deal with dups

        //not sure if there is more that we will have to do with this later, currently it updates
        //things locally but what about elastic? not that we currently deal with ride lists of users...
        //rider.confirmDriver(ride);
        //another cant do while the ride array list of rider is null

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride",ride.getId().toString(), rideString);

    }
}
