package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * Created by jferris on 22/10/16.
 */
public class Rider extends User {
    @JestId
    private String elasticID;
    public ArrayList<Ride> rideArrayList;
    public ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.rideArrayList = new ArrayList<>();
        this.requestArrayList = new ArrayList<>();
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

    public void setRideArrayList(ArrayList<Ride> rideArrayList) {
        this.rideArrayList = rideArrayList;
    }

    public ArrayList<Request> getRequestArrayList() {
        return requestArrayList;
    }

    public void setRequestArrayList(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }


    public void acceptRideOffer(Driver driver) {
    }


    public void removeRequest(Request currentRequest) {
    }

    public void confirmDriver(Driver driver){

    }
}
