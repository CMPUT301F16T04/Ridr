package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Ride {
    private String pickup;
    private String dropoff;
    private Date rideDate;
    private String driver;
    private String rider;
    private Boolean isCompleted; //pending is denoted by isCompleted = False
    private LatLng pickupCoords;
    private LatLng dropOffCoords;
    private UUID id;
    private float fare;


    public LatLng getPickupCoords() {
        return pickupCoords;
    }

    public void setPickupCoords(LatLng pickupCoords) {
        this.pickupCoords = pickupCoords;
    }

    public LatLng getDropOffCoords() {
        return dropOffCoords;
    }

    public void setDropOffCoords(LatLng dropOffCoords) {
        this.dropOffCoords = dropOffCoords;
    }



    public Ride(String driverId, String riderId, String pickup, String dropoff, Date date, LatLng pickupCoords, LatLng dropOffCoords){
        this.rideDate = date;
        this.driver = driverId;
        this.rider = riderId;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupCoords = pickupCoords;
        this.dropOffCoords = dropOffCoords;
        this.isCompleted = false;
        this.id = UUID.randomUUID();
        this.fare  = 20;

    }


    public float getFare() {
        return fare;
    }


    public UUID getId(){return id;}

    public String getPickupAddress() {
        return pickup;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver.getID().toString();
    }

    public String getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider.getID().toString();
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public String getDropOffAddress() {
        return dropoff;
    }

    public boolean pushAcceptedByRider() {
        return false;
    }

    public void complete() {
        this.isCompleted = true;
    }

    public boolean hasDriver(Driver driver){
        return false;
    }

    public boolean equals(Ride ride) {
        return this.id.equals(ride.id);
    }

    //stolen directly from Justin's implementation in request, then made sure custom to ride attributes
    // and database ride properties
    public String toJsonString(){
        // Attempt to conver request into a JsonObject
        // If fail return a null pointer
        // Need to use the java standard JSON object here because we are nesting JSON items
        JSONObject toReturn = new JSONObject();
        try {
            toReturn.put("rider", this.rider);
            toReturn.put("driver", this.driver);
            toReturn.put("pickup", this.pickup);
            toReturn.put("dropoff", this.dropoff);
            toReturn.put("pickupCoords", buildGeoPoint(pickupCoords));
            toReturn.put("dropOffCoords", buildGeoPoint(dropOffCoords));
            toReturn.put("id", this.id.toString());
            toReturn.put("isCompleted", this.isCompleted);
            toReturn.put("date", rideDate.toString());
            toReturn.put("fare", fare);
            return toReturn.toString();
        } catch(Exception e){
            Log.d("Error", e.toString());
            return null;

        }
    }

    // Take a jsonObject as input and creates request out of it's keys
    public Ride(JsonObject ride) throws ParseException {
        // There is one major limitation in what I have done so far,
        // currently I don't have or store a list of possible drivers
        // Because of the differences between JsonObject and JSONObject.
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

        this.rider = ride.get("rider").getAsString();
//        this.driver = ride.get("driver").getAsString();
        this.driver = "Bob";
        this.pickup = ride.get("pickup").getAsString();
        this.dropoff = ride.get("dropoff").getAsString();
        this.dropOffCoords = buildLatLng(ride.getAsJsonObject("dropOffCoords"));
        this.pickupCoords = buildLatLng(ride.getAsJsonObject("pickupCoords"));
        this.isCompleted = ride.get("isCompleted").getAsBoolean();
        this.rideDate = formatter.parse(ride.get("date").getAsString());
        this.id = UUID.fromString(ride.get("id").getAsString());
        this.fare = ride.get("fare").getAsFloat();

    }

    private JSONObject buildGeoPoint(LatLng coords) throws JSONException {
        JSONObject newLatLng = new JSONObject();
        newLatLng.put("lat", coords.latitude);
        newLatLng.put("lon", coords.longitude);
        return newLatLng;

    }
    private LatLng buildLatLng(JsonObject coords){
        return new LatLng(coords.get("lat").getAsDouble(), coords.get("lon").getAsDouble());
    }
}
