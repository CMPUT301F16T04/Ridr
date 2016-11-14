package ca.ualberta.ridr;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16.
 * This controller controls access to all requests used by a view
 * This controllers uses threads to asynchronously perform network requests
 * It then uses a callback interface to inform the view that is using the controller that data has
 * been updated
 */
public class RequestController {

    private ArrayList<Request> requests;
    private ACallback cbInterface;

    RequestController(){}

    public RequestController(ACallback cbInterface){
        this.cbInterface = cbInterface;
        this.requests = new ArrayList<>();
    }

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


    public int size(){
        return requests.size();
    }

    public void add(Request request){
        requests.add(request);
        cbInterface.callback();
    }
    public ArrayList<Request> getList(){
        return requests;
    }

    /**
     * This function searches for all requests made by a user
     * @param userID
     */
    public void getUserRequest(final UUID userID) {
        // Get all user requests from the database
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", userID.toString());
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.callback();
    }

    /**
     * A function that gets all current requests available
     * As a fix we may eventually want to limit this to a certain radius around the user
     */
    public void getAllRequests() {
            // Get all user requests from the database
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndex("request");
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.callback();
    }

    /**
     * Find all requests within a certain distance.
     *
     * @param center   a center point to search around
     * @param distance distance of the point to filter requests from
     */
    public void findAllRequestsWithinDistance(final LatLng center, final String distance){
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.geoDistanceQuery("request", center, distance);
        requests.clear();
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.callback();

    }
}
