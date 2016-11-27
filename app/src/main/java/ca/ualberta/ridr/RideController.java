package ca.ualberta.ridr;

import android.util.Log;

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

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride",ride.getId().toString(), rideString);

    }
}
