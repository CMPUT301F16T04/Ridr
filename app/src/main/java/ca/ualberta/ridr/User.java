package ca.ualberta.ridr;

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

    public User(String name, Date dateOfBirth, String creditCard, ArrayList<Ride> rideArrayList){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.creditCard = creditCard;
        this.rideArrayList = rideArrayList;
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
}
