package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private String ridr;
    private String pickup;
    private String dropoff;
    private ArrayList<Driver> possibleDrivers;
    private Boolean accepted;
    private Driver driver;

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

    public String getRidr() {
        return ridr;
    }

    public void setRidr(String ridr) {
        this.ridr = ridr;
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
