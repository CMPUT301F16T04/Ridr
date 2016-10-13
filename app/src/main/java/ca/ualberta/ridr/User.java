package ca.ualberta.ridr;

import java.sql.RowIdLifetime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mackenzie on 12/10/16.
 */
public class User {
    public String name;
    public Date dateOfBirth;
    public String creditCard;
    public ArrayList<Ride> rideArrayList;
    public ArrayList<Request> requests;

    public User(String name, Date dateOfBirth, String creditCard){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.creditCard = creditCard;
        this.rideArrayList = new ArrayList<Ride>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public ArrayList<Ride> getRideArrayList() {
        return rideArrayList;
    }

    public void setRideArrayList(ArrayList<Ride> rideArrayList) {
        this.rideArrayList = rideArrayList;
    }

    public Ride createRide() {
        //Dont know how to fix without actually writing code
        return;
    }

    public void acceptRideOffer(Driver driver) {
    }

    public void requestRide(String s, String s1) {
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }
}
