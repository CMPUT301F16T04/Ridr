package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private String rider;
    private String pickup;
    private String dropoff;
    private LatLng pickupPos;
    private LatLng dropoffPos;
    private ArrayList<Driver> possibleDrivers;
    private Boolean accepted;
    private UUID id;

    Request(String pickup, String dropoff){
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.id = UUID.randomUUID();
    }

    Request(String pickup, String dropoff, LatLng pickupPos, LatLng dropoffPos){
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupPos = pickupPos;
        this.dropoffPos = dropoffPos;
        this.id = UUID.randomUUID();
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

    public LatLng getPickupPos() {
        return pickupPos;
    }

    public LatLng getDropoffPos(){
        return dropoffPos;
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
}
