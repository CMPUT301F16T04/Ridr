package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private Rider rider;
    private String pickup;
    private String dropoff;
    private ArrayList<Driver> possibleDrivers;
    private Boolean accepted;

    Request(Rider rider, String pickup, String dropoff){
        this.rider = rider;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.accepted = false;
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

    public Rider getRider() {
        return this.rider;
    }

    public void setRider(Rider rider) {
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
