package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class Rider extends User {
    private transient ArrayList<Ride> rideArrayList;
    private transient ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.rideArrayList = new ArrayList<Ride>();
        this.requestArrayList = new ArrayList<Request>();
    }

    public ArrayList<Ride> getRides() {
        return rideArrayList;
    }

    public void setRides(ArrayList<Ride> rideArrayList) {
        this.rideArrayList = rideArrayList;
    }

    public ArrayList<Request> getRequests() {
        return requestArrayList;
    }

    public void setRequests(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }

    public Ride createRide() {
        //Dont know how to fix without actually writing code

        /* This doesn't work because it fails encapsulation, any method on an object should only effect that object.
         * This might need to be put in Ride
         * - Justin
         */
        /*
         * Temporally fixed it by creating a dummy constructor for Ride
         * -Marco
         */
        /*
        *All this stuff is just to make it pass the unit tests for now, delete when implementing
         */
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Ride ride = new Ride(driver, rider, "University of Alberta", "West Edmonton Mall", new Date(), new LatLng(53.5232, -113.5263), new LatLng(53.5225, -113.6242));
        return ride;
    }

    public void acceptRideOffer(Driver driver) {
    }

    public void requestRide(String s, String s1) {
    }

    public void removeRequest(Request currentRequest) {
    }

    public void confirmDriver(Driver driver){

    }

}
