package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Driver extends User {

    @JestId
    private String driverID;
    public Vehicle vehicle;
    public ArrayList<Ride> rideArrayList;

    public Driver(String name, Date dateOfBirth, String creditCard,
                  String email, String phoneNumber, Vehicle vehicle) {
        super(name, dateOfBirth, creditCard, email, phoneNumber);
        this.vehicle = vehicle;
        this.rideArrayList = new ArrayList<Ride>();
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
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

    public boolean isOffline() {
        return false;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}


