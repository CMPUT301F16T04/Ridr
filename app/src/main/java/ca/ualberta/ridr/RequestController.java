package ca.ualberta.ridr;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController {
    RequestController(){}

    public ArrayList<Driver> getPossibleDrivers(Request request){return(request.getPossibleDrivers());}

    public void removeRequest(Request request, Rider rider){rider.removeRequest(request);}

    public Request getRequestFromServer(String requestId) {
        AsyncController con = new AsyncController();
        try {
            JsonObject requestObj = con.get("request", "id", requestId).getAsJsonObject();
            Request request = new Request(requestObj);
            return(request);
        }catch (Exception e) {
            Log.i("Error parsing requests", e.toString());
        }
        return(null);
    }

    public void setRequestAccepted(Request request) {
        request.setAccepted(Boolean.TRUE);
    }
}
