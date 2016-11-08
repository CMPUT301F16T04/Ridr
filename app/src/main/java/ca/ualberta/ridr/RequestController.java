package ca.ualberta.ridr;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    RequestController(){}

    //actually should I have this in teh request controller or should it be in the Rider controller?
    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

    public ArrayList<Driver> getPossibleDrivers(Request request){return(request.getPossibleDrivers());}

    public void removeRequest(Request request, Rider rider){rider.removeRequest(request);}

}
