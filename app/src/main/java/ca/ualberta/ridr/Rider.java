package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by jferris on 22/10/16.
 * Worked on by Marc-O and Kristy on 03/11/2016
 */
public class Rider extends User {
    @JestId
    private String elasticID;
    private transient ArrayList<Ride> rideArrayList;
    private transient ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.rideArrayList = new ArrayList<Ride>();
        this.requestArrayList = new ArrayList<Request>();
        super.setRiderStatus(true);
    }

    public Rider (User user){
        super(user);
        super.setRiderStatus(true);
    }
    public void setRiderStatus(boolean status) {
        super.setRiderStatus(status);
    }

    public String getElasticID() {
        return elasticID;
    }

    public void setElasticID(String elasticID) {
        this.elasticID = elasticID;
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

    public UUID getId(){return super.getID();}

    public void setRequests(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }


    public void acceptRideOffer(Driver driver) {
    }

    public void requestRide(String s, String s1) {
    }

    public void removeRequest(Request currentRequest) {
        requestArrayList.remove(currentRequest);
    }

    public void confirmDriver(Ride ride){
            rideArrayList.add(ride);

    }

    public void addRequest(Request request){
        requestArrayList.add(request);
    }


}
