package ca.ualberta.ridr;

import java.util.Date;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Ride {
    private String pickup;
    private String dropoff;
    private Date rideDate;
    private Driver driver;
    private Rider rider;
    private Boolean isCompleted; //pending is denoted by isCompleted = False
    public String pickupAddress;
    public String dropOffAddress;

    public Ride(Driver driver, Rider rider, String pickup, String dropoff, Date date){
        this.rideDate = date;
        this.driver = driver;
        this.rider = rider;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.isCompleted = false;
    }

    public Double getFare() {
        Double fare = 0.0;
        return fare;
    }

    public String getPickupAddress() {
        return pickup;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
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

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
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

}
