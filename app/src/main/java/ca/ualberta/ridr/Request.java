package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *
 *
 */
public class Request {
    private String rider; //string of the id
    private String pickup;
    private String dropoff;
    private LatLng pickupCoord;
    private LatLng dropOffCoord;
    private transient ArrayList<Driver> possibleDrivers;
    private Boolean accepted;
    private UUID id;
    private float fare;
    private Date date;


    Request(String rider, String pickup, String dropoff, LatLng pickupCoords, LatLng dropOffCoords, Date date){
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupCoord = pickupCoords;
        this.dropOffCoord = dropOffCoords;
        this.date = date;
        this.rider = rider;
        this.id = UUID.randomUUID();
        this.date = date;
        this.fare = 20;
        this.accepted = false;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
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

    public void setFare(float estimate){
        this.fare = estimate;
    }

    public float estimateFare(float distance){
        float gasCostFactor = 4; // calculate something later
        //return distance * gasCostFactor;
        float tempVal = 20;
        return tempVal;
    }

    public UUID getID() {
        return id;
    }

    public String toJsonString(){
        // Attempt to convert request into a JsonObject
        // If fail return a null pointer
        // Need to use the java standard JSON object here because we are nesting JSON items
        JSONObject toReturn = new JSONObject();
        try {
            toReturn.put("rider", this.rider);
            toReturn.put("pickup", this.pickup);
            toReturn.put("dropoff", this.dropoff);
            toReturn.put("pickupCoord", buildGeoPoint(pickupCoord));
            toReturn.put("dropOffCoord", buildGeoPoint(dropOffCoord));
            toReturn.put("id", this.id.toString());
            toReturn.put("accepted", this.accepted);
            toReturn.put("date", date.toString());
            toReturn.put("fare", fare);
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
        // Because of the differences between JsonObject and JSONObject.
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

        this.rider = request.get("rider").getAsString();
        this.pickup = request.get("pickup").getAsString();
        this.dropoff = request.get("dropoff").getAsString();
        this.dropOffCoord = buildLatLng(request.getAsJsonObject("dropOffCoord"));
        this.pickupCoord = buildLatLng(request.getAsJsonObject("pickupCoord"));
        this.accepted = request.get("accepted").getAsBoolean();
        this.date = formatter.parse(request.get("date").getAsString());
        this.id = UUID.fromString(request.get("id").getAsString());
        this.fare = request.get("fare").getAsFloat();
       // this.possibleDrivers =  buildPossibleDriversList(request.getAsJsonArray("possibleDrivers"));
        // maybe one day we will reach this dream

    }

    /**
     * This returns the String fields that are queryable for keyword search in a request
     * @return ArrayList<String>
     */
    public ArrayList<String> queryableRequestVariables() {
        ArrayList<String> stringArray = new ArrayList<>();

        if(this.dropoff != null) {
            stringArray.add(this.dropoff);
        }

        if(this.pickup != null) {
            stringArray.add(this.pickup);
        }

        if(this.rider != null) {
            stringArray.add(this.rider);
        }

        stringArray.add(Float.toString(this.fare));

        return stringArray;
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

    //also a faroff dream
    //intentions : to be able to store and retrieve a list of possible drivers.
//    private ArrayList<Driver> buildPossibleDriversList(JsonArray array){
//        ArrayList<Driver> drivers = new ArrayList<Driver>();
//
//        return(drivers);
//    }

}
