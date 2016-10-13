package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Driver {
    public String name;
    public Date dateOfBirth;
    public String vehicle;
    public String bankAccountNo;

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

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public ArrayList<Ride> getRideArrayList() {
        return rideArrayList;
    }

    public void setRideArrayList(ArrayList<Ride> rideArrayList) {
        this.rideArrayList = rideArrayList;
    }

    public ArrayList<Ride> rideArrayList;


}
