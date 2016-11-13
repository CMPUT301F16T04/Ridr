package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/*
 * This controller controls access to all requests used by a view
 * This controllers uses threads to asynchronously perform network requests
 * It then uses a callback interface to inform the view that is using the controller that data has
 * been updated
 */
public class RequestController {
    private Request currenRequest;
    private ArrayList<Request> requests;
    private ACallback cbInterface;

    /**
     * Creates a new request
     * @param rider Rider object. currently logged in rider who is creating the request
     * @param pickup String of the pickup address
     * @param dropoff String of the dropoff address
     * @param pickupCoords Coordinates of pickup location
     * @param dropOffCoords Coordinates of dropoff location
     * @param date Date at which the rider wishes to be picked up
     */
    public void createRequest(Rider rider, String pickup, String dropoff,LatLng pickupCoords, LatLng dropOffCoords, Date date){
        AsyncController controller = new AsyncController();
        currenRequest = new Request(rider, pickup, dropoff, pickupCoords, dropOffCoords, date);
        rider.addRequest(currenRequest);
        this.add(currenRequest);

        //TODO send request to server
    }

    /**
     * Estimates a fare based on distance
     * @param distance distance from pickup to dropoff
     * @return a recommended fare
     */
    public float getFareEstimate(float distance){
        return currenRequest.estimateFare(distance);
    }

    public void updateFare(float fare) {
        currenRequest.setFare(fare);
    }


    public RequestController(ACallback cbInterface){
        this.cbInterface = cbInterface;
        this.requests = new ArrayList<>();
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
