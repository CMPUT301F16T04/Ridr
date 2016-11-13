package ca.ualberta.ridr;

import android.util.Log;

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
