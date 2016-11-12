package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

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
    private LatLng pickupCoords;
    private LatLng dropOffCoords;
    private transient ArrayList<Driver> possibleDrivers;
    private Boolean accepted;
    private UUID id;
    private Date date;




    Request(String pickup, String dropoff, LatLng pickupCoords, LatLng dropOffCoords, Date date){
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupCoords = pickupCoords;
        this.dropOffCoords = dropOffCoords;
        this.date = date;
        this.id = UUID.randomUUID();
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



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
}
