package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class Rider extends User {
    public ArrayList<Ride> rideArrayList;
    public ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.rideArrayList = new ArrayList<>();
        this.requestArrayList = new ArrayList<>();
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

    public void acceptRideOffer(Driver driver) {
    }

    public void removeRequest(Request currentRequest) {
    }

    public void confirmDriver(Driver driver){

    }

}
