package ca.ualberta.ridr;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16, modified by Justin Barclay
 * This is a controller for the rides object that can fetch rides from the server
 * Store them in an array and modify rides and store them on the server
 *
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
        controller = new AsyncController();
    }

    public ArrayList<Ride> getAll() {
        return rides;
    }

    public void createRide(String driverName, Request request, String riderName) {
        //will need to replace the date I guess with actual date that ride is supposed to occur
        Ride ride = new Ride(driverName, riderName,  request.getPickup(), request.getDropoff(), request.getDate() , request.getPickupCoords(), request.getDropOffCoords());

        String rideString = ride.toJsonString();
        AsyncController con = new AsyncController();
        JsonObject s = con.create("ride", ride.getId().toString(), rideString);

    }


    /**
     * Gets all rides for a driver from the server
     * If a parsing a ride fails log it.
     * @param userID the user id
     */
    public void getDriverRides(final String userID) {
        // Get all user rides from the database
        AsyncController controller = new AsyncController();
        try {
            JsonArray queryResults = controller.getAllFromIndexFiltered("ride", "driver", userID);
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

    /**
     * Get all rider rides for a particular rider
     * Log any exceptions to console
     * @param userID the user id
     */
    public void getRiderRides(final String userID) {
        // Get all user rides from the database
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


    /**
     * Use merge sort to sort a rides array list by uncompleted, completed, and completed and paid.
     *
     * @param toSort the to sort
     * @return the array list
     */
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

    /**
     * Helper function for merge sort
     * @param left
     * @param right
     * @return
     */
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

    /**
     * Get a single ride from the rides array list
     * @param id
     * @return
     */
    @Nullable
    public Ride getRide(String id){
        Ride ride = findRideInList(id);
        return ride;
    }

    /**
     * Grab a single ride from the server
     * @param rideID
     */
    public void findRide(String rideID) {
        JsonObject ride = controller.get("ride", "id", rideID);
        try {
            Ride aRide = new Ride(ride);
            rides.add(new Ride(ride));
            cbInterface.update();
        } catch (Exception e){
            Log.i("Failed to make ride", String.valueOf(e));
        }
    }

    /**
     * Complete a ride and then notify the server the ride is complete
     * @param rideid
     */
    public void completeRide(String rideid){
        Ride ride = findRideInList(rideid);
        ride.setCompleted(true);
        ride.setPaid(true);
        controller.create("ride", ride.getId().toString(), ride.toJsonString());
    }

    /**
     * Helper function to find all rides in a list
     * @param id
     * @return
     */
    @Nullable
    private Ride findRideInList(String id){
        for(int i=0; i<rides.size(); ++i){
            Ride ride = rides.get(0);
            if(ride.getId().equals(UUID.fromString(id))){
                return ride;
            }
        }
        return null;
    }
}
