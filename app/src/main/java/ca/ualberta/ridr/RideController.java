package ca.ualberta.ridr;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    ArrayList<Ride> rides;
    ACallback cbInterface;
    AsyncController controller;

    RideController() {
    }

    RideController(ACallback rideInterface) {
        this.cbInterface = rideInterface;
        this.rides = new ArrayList<>();
    }

    public ArrayList<Ride> getAll() {
        return rides;
    }

    public void createRide(String driverId, Request request, String riderId) {
        //will need to replace the date I guess with actual date that ride is supposed to occur
        Ride ride = new Ride(driverId, riderId, request.getPickup(), request.getDropoff(), new Date(), request.getPickupCoords(), request.getDropOffCoords());

        //rider.confirmDriver(ride);
        //another cant do while the ride array list of rider is null

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride", ride.getId().toString(), rideString);

    }

    public void getDriverRides(final UUID userID) {
        // Get all user requests from the database
        AsyncController controller = new AsyncController();
        try {
            JsonArray queryResults = controller.getAllFromIndexFiltered("ride", "driver", userID.toString());
            for (JsonElement result : queryResults) {
                try {
                    rides.add(new Ride(result.getAsJsonObject().getAsJsonObject("_source")));
                } catch (Exception e) {
                    Log.i("Error parsing requests", e.toString());
                }
            }
            cbInterface.update();
        } catch (Exception e) {
            Log.i("Null request", e.toString());
        }
    }

    public void getRiderRides(final UUID userID) {
        // Get all user requests from the database
        AsyncController controller = new AsyncController();
        try {
            JsonArray queryResults = controller.getAllFromIndexFiltered("ride", "rider", userID.toString());
            for (JsonElement result : queryResults) {
                try {
                    rides.add(new Ride(result.getAsJsonObject().getAsJsonObject("_source")));
                } catch (Exception e) {
                    Log.i("Error parsing requests", e.toString());
                }
            }
            cbInterface.update();
        } catch (Exception e) {
            Log.i("Null request", e.toString());
        }
    }
}