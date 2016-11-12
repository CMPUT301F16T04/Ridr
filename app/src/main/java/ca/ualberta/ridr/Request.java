package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private String rider;
    private String pickup;
    private String dropoff;
    private LatLng pickupCoord;
    private LatLng dropOffCoord;
    private transient ArrayList<Driver> possibleDrivers;
    private Boolean accepted;
    private UUID id;
    private float fare;
    private Date date;

    Request(Rider rider, String pickup, String dropoff, LatLng pickupCoords, LatLng dropOffCoords, Date date){
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupCoord = pickupCoords;
        this.dropOffCoord = dropOffCoords;
        this.date = date;
        this.rider = rider.getID().toString();
        this.id = UUID.randomUUID();
        this.fare = 20;
        this.accepted = false;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public LatLng getPickupCoords() {
        return pickupCoord;
    }

    public void setPickupCoords(LatLng pickupCoords) {
        this.pickupCoord = pickupCoords;
    }

    public LatLng getDropOffCoords() {
        return dropOffCoord;
    }

    public void setDropOffCoords(LatLng dropOffCoords) {
        this.dropOffCoord = dropOffCoords;
    }
    public boolean equals(Request request) {
        return this.id.equals(request.id);
    }
    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }


    public String getPickup() {
        return pickup;
    }

    public String getDropoff(){
        return dropoff;
    }

    public void addAccepted(Driver driver) {
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public ArrayList<Driver> getPossibleDrivers() {
        return possibleDrivers;
    }

    public void setPossibleDrivers(ArrayList<Driver> drivers) {
        this.possibleDrivers = drivers;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isSent() {
        return false;
    }

    public float getFare(){
        return fare;
    }
    public UUID getID() {
        return id;
    }

    public String toJson(){
        // Attempt to conver request into a JsonObject
        // If fail return a null pointer
        JsonObject toReturn = new JsonObject();
        try {
            toReturn.addProperty("rider", this.rider);
            toReturn.addProperty("pickup", this.pickup);
            toReturn.addProperty("dropoff", this.dropoff);
            toReturn.addProperty("pickupCoord", String.valueOf(buildGeoPoint(pickupCoord)));
            toReturn.addProperty("dropoffCoord", String.valueOf(buildGeoPoint(dropOffCoord)));
            toReturn.addProperty("id", this.id.toString());
            toReturn.addProperty("accepted", this.accepted);
            toReturn.addProperty("date", date.toString());
            toReturn.addProperty("fare", fare);
            return toReturn.toString();
        } catch(Exception e){
            Log.d("Error", e.toString());
            return null;

        }
    }

    // Take a jsonObject as input and creates request out of it's keys
    public Request(JsonObject request) throws ParseException {
        // There is one major limitation in what I have done so far,
        // currently I don't have or store a list of possible drivers
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

        this.rider = request.get("rider").getAsString();
        this.pickup = request.get("pickup").getAsString();
        this.dropoff = request.get("dropoff").getAsString();
        this.dropOffCoord = buildLatLng(request.getAsJsonObject("dropoffCoord"));
        this.pickupCoord = buildLatLng(request.getAsJsonObject("pickupCoord"));
        this.accepted = request.get("accepted").getAsBoolean();
        this.date = formatter.parse(request.get("date").getAsString());
        this.id = UUID.fromString(request.get("id").getAsString());
        this.fare = request.get("fare").getAsFloat();

    }

    private JsonObject buildGeoPoint(LatLng coords){
        JsonObject newLatLng = new JsonObject();
        newLatLng.addProperty("lat", coords.latitude);
        newLatLng.addProperty("lon", coords.longitude);
        return newLatLng;

    }
    private LatLng buildLatLng(JsonObject coords){
        return new LatLng(coords.get("lat").getAsDouble(), coords.get("lon").getAsDouble());
    }
}
