package ca.ualberta.ridr;

/**
 * Created by mackenzie on 12/10/16.
 */
public class Vehicle {
    public int year;
    public String make;
    public String model;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Vehicle(int year, String make, String model){
        this.year = year;
        this.make = make;
        this.model = model;

    }
}
