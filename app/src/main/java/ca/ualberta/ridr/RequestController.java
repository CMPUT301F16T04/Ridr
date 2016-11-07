package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    RequestController(){}

    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

    public ArrayList<Driver> getPossibleDrivers(Request request){return(request.getPossibleDrivers());}

    public String getPickup(Request request){return(request.getPickup());}

    public String getDropoff(Request request){return(request.getDropoff());}

    //think we need...
    public void removeRequest(Request request, Rider rider){rider.removeRequest(request);}

}
