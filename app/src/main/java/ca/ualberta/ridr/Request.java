package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private String rider;
    private String pickup;
    private String dropoff;
    private ArrayList<Driver> possibleDrivers;
    private Boolean accepted;

    Request(String pickup, String dropoff){
        this.pickup = pickup;
        this.dropoff = dropoff;

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
