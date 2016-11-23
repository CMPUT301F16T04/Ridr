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
            rides = sortRides(rides);
            cbInterface.update();
        } catch (Exception e) {
            Log.i("Null request", e.toString());
        }
    }
    public ArrayList<Ride> sortRides(ArrayList<Ride> toSort){
        if(toSort.size() <= 1){
            return toSort;
        }
        ArrayList<Ride> left = new ArrayList<>();
        ArrayList<Ride> right = new ArrayList<>();

        for(int i=0; i < toSort.size(); ++i){
            if(i< toSort.size()/2){
                left.add(toSort.get(i));
            } else {
                right.add(toSort.get(i));
            }
        }

        left = sortRides(left);
        right = sortRides(right);
        return mergeRides(left, right);
    }
    private ArrayList<Ride> mergeRides(ArrayList<Ride> left, ArrayList<Ride> right){
        ArrayList<Ride> result = new ArrayList<>();

        while(!left.isEmpty() && !right.isEmpty()){
            if(!left.get(0).isCompleted()){
                result.add(left.get(0));
                left.remove(0);
            } else if(!right.get(0).isCompleted()){
                result.add(right.get(0));
                right.remove(0);
            } else if(left.get(0).isCompleted() && !left.get(0).isPaid()){
                result.add(left.get(0));
                left.remove(0);
            } else if(right.get(0).isCompleted() && !right.get(0).isPaid()){
                result.add(right.get(0));
                right.remove(0);
            } else if(left.get(0).isPaid()){
                result.add(left.get(0));
                left.remove(0);
            } else if(right.get(0).isPaid()){
                result.add(right.get(0));
                right.remove(0);
            }
        }

        while(!left.isEmpty()){
            result.add(left.get(0));
            left.remove(0);
        }
        while(!right.isEmpty()){
            result.add(right.get(0));
            right.remove(0);
        }

        return result;
    }

}