package ca.ualberta.ridr;

/**
 * Created by Justin on 2016-10-12.
 */
public class Request {
    private String pickup;
    private String dropoff;

    Request(String pickup, String Dropoff){
        this.pickup = pickup;
        this.dropoff = dropoff;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDropoff(){
        return dropoff;
    }
}
