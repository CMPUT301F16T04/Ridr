package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Driver extends User {
    // These are all marked transient as we don't want them serialized when we pass this object to
    // our AsyncDatabaseController
    private transient Vehicle vehicle;
    private transient String bankAccountNo;
    private transient ArrayList<Ride> rideArrayList;

    public Driver(String name, Date dateOfBirth, String creditCard,
                  String email, String phoneNumber, Vehicle vehicle, String bankAccountNo) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.vehicle = vehicle;
        this.bankAccountNo = bankAccountNo;
        this.rideArrayList = new ArrayList<Ride>();
    }

    public String getName() {
        return this.getName();
    }

    public void setName(String name) {
        this.setName(name);
    }

    public Date getDateOfBirth() {
        return this.getDateOfBirth();
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
    }


    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public ArrayList<Ride> getRides() {
        return rideArrayList;
    }

    public void setRides(ArrayList<Ride> rideArrayList) {
        this.rideArrayList = rideArrayList;
    }

    public void addRide() {
    }

    public boolean kewordSearch(String input) {
        return false;
    }

    public boolean geoSort(String s, String myLocation) {
        return false;
    }

    public void acceptRide(Ride ride) {
    }

    public boolean completeRide(Ride ride) {
        return false;
    }

    public boolean isPayed() {
        return false;
    }

    public boolean getPendingRides() {
        return false;
    }

    public boolean getCompletedRides() {
        return false;
    }

    public boolean riderAcceptedRide(Ride ride) {
        return false;
    }

    public String getPhoneNumber() {
        return this.getPhoneNumber();
    }

    public String getEmail() {
        return this.getEmail();
    }

    public boolean isOffline() {
        return false;
    }

}
