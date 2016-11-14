package ca.ualberta.ridr;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by jferris on 22/10/16.
 */
public class RiderController {
    RiderController(){
    }
    public Rider getRiderFromServer(String riderId){
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "id", riderId), Rider.class);
        return(rider);
    }
    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

}

