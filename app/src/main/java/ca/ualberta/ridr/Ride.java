package ca.ualberta.ridr;

import java.util.Date;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Ride {
    public Date rideDate;
    public Driver driver;
    public User user;
    public Boolean isCompleted; //pending is denoted by isCompleted = False

    public Ride(Date rideDate, Driver driver, User user, Boolean isCompleted){
        this.rideDate = rideDate;
        this.driver = driver;
        this.user = user;
        this.isCompleted = isCompleted;
    }

    public String getPickupAddress() {
        return pickupAddress;
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
        return dropOffAddress;
    }

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }

    public String pickupAddress;
    public String dropOffAddress;

}
