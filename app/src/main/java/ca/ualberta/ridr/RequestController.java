package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16.
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

    // Pretty hard to test
    public void getUserRequest(final UUID userID) {
        // Get all user requests from the database
        Thread getUser = new Thread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

        public void getAllRequests() {
            // Get all user requests from the database
            Thread getUser = new Thread(new Runnable() {
                @Override
                public void run() {
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
        });

        getUser.start();
    }

    // Pretty hard to test
    public void findAllRequestsWithinDistance(final LatLng center, final String distance){
        Thread getUser = new Thread(new Runnable() {
            @Override
            public void run() {
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
        });
        getUser.start();
    }
}
