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
    public String pickupAddress;
    public String dropOffAddress;
    
}
