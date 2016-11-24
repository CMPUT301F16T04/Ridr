package ca.ualberta.ridr;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/*
=======
/**
 * This controller controls access to all requests used by a view
 * This controllers uses threads to asynchronously perform network requests
 * It then uses a callback interface to inform the view that is using the controller that data has
 * been updated
 */
public class RequestController {

    private Request currenRequest;
    private JsonArray jsonArray;
    private ArrayList<Request> requests;
    private ACallback cbInterface;
    Context context;

    public RequestController(Context context) {
        this.context = context;
    }

    public RequestController(ACallback cbInterface, Context context){
        this.cbInterface = cbInterface;
        this.requests = new ArrayList<>();
        this.context = context;
    }

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
        AsyncController controller = new AsyncController(this.context);
        currenRequest = new Request(rider.getID().toString(), pickup, dropoff, pickupCoords, dropOffCoords, date);
        rider.setRequests(new ArrayList<Request>());
        //commented for now so that we can actually create the request without breaking
        //rider.addRequest(currenRequest);
        this.add(currenRequest);

        try{
            controller.create("request", currenRequest.getID().toString(), currenRequest.toJsonString());
        } catch (Exception e){
            Log.i("Error creating request", e.toString());
        }
    }

    /**
     * Estimates a fare based on distance
     * @param distance distance from pickup to dropoff
     * @return a recommended fare
     */
    public float getFareEstimate(float distance){
        return currenRequest.estimateFare(distance);
    }

    public void updateFare(float newFare) {
        currenRequest.setFare(newFare);
    }

    /**
     * Uses a keyword to search through the queryable fields for requests
     * Returns the requests containing the keyword in one or more of the fields
     * Does not return duplicates of the same request if multiple instances of keyword
     * @param keyword
     * @return ArrayList<Request>
     */
    public ArrayList<Request> searchRequestsKeyword(String keyword) {
        jsonArray = new AsyncController(this.context).getAllFromIndex("request");
        ArrayList<Request> requestsKeyword = new ArrayList<>();
        Request request;

        for (JsonElement element: jsonArray) {
            if(doesJsonContainKeyword(keyword, element)) {
                try {
                    request = new Request(element.getAsJsonObject().getAsJsonObject("_source"));
                    requestsKeyword.add(request);
                } catch(Exception e) {
                    Log.i("Error returning keyword", e.toString());
                }
            }
        }
        return requestsKeyword;
    }

    /**
     * Checks if the JsonElement for request contains the string keyword
     * @param keyword
     * @param jsonElement
     * @return Boolean
     */
    public Boolean doesJsonContainKeyword(String keyword, JsonElement jsonElement) {
        ArrayList<String> stringArray;
        keyword = keyword.toLowerCase();
        Pattern p = Pattern.compile(keyword);
        Request request;
        try {
            request = new Request(jsonElement.getAsJsonObject().getAsJsonObject("_source"));
            stringArray = request.queryableRequestVariables();
            for (String s : stringArray) {
                s = s.toLowerCase();
                if (p.matcher(s).find()) {
                    return true;
                }
            }
        } catch(Exception e) {
            Log.i("Error searching keyword", e.toString());
        }

        return false;
    }



    public ArrayList<Driver> getPossibleDrivers(Request request){return(request.getPossibleDrivers());}

    public void removeRequest(Request request, Rider rider){rider.removeRequest(request);}

    public Request getRequestFromServer(String requestId) {
        AsyncController con = new AsyncController(this.context);
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
        cbInterface.update();
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
        AsyncController controller = new AsyncController(this.context);
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", userID.toString());
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.update();
    }

    /**
     * A function that gets all current requests available
     * As a fix we may eventually want to limit this to a certain radius around the user
     */
    public void getAllRequests() {
            // Get all user requests from the database
        AsyncController controller = new AsyncController(this.context);
        JsonArray queryResults = controller.getAllFromIndex("request");
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.update();
    }

    /**
     * Find all requests within a certain distance.
     *
     * @param center   a center point to search around
     * @param distance distance of the point to filter requests from
     */
    public void findAllRequestsWithinDistance(final LatLng center, final String distance){
        AsyncController controller = new AsyncController(this.context);
        JsonArray queryResults = controller.geoDistanceQuery("request", center, distance);
        requests.clear();
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.update();

    }
}
