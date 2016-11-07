package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16.
 */
public class Rider extends User {
    private ArrayList<Ride> rideArrayList;
    private ArrayList<Request> requestArrayList;

    public Rider (String name, Date dateOfBirth, String creditCard, String email, String phoneNumber) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.rideArrayList = new ArrayList<>();
        this.requestArrayList = new ArrayList<>();
    }

    public ArrayList<Ride> getRideArrayList() {
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
}
