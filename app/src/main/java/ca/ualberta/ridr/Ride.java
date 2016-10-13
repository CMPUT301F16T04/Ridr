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
    private User user;
    private Boolean isCompleted; //pending is denoted by isCompleted = False

    public Ride(Date rideDate, Driver driver, User user, Boolean isCompleted){
        this.rideDate = rideDate;
        this.driver = driver;
        this.user = user;
        this.isCompleted = isCompleted;
    }

    public Ride(Driver driver, User user, String pickup, String dropoff, Date date){
        this.rideDate = date;
        this.driver = driver;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String pickupAddress;
    public String dropOffAddress;

    public boolean pushAcceptedByUser() {
        return false;
    }

    public void complete() {
        this.isCompleted = true;
    }
}
