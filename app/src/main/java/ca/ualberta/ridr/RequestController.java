package ca.ualberta.ridr;

import java.util.ArrayList;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    RequestController(){}

    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

    public ArrayList<Driver> getPossibleDrivers(Request request){return(request.getPossibleDrivers());}
}
