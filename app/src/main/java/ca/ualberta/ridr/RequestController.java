package ca.ualberta.ridr;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import android.util.Log;
import android.widget.Toast;

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
    private ArrayList<Request> offlineRequests;
    private ACallback cbInterface;
    private Context context;
    private OfflineSingleton offlineSingleton = OfflineSingleton.getInstance();

    public RequestController(Context context) {
        this.context = context;
        this.offlineRequests = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    public RequestController(ACallback cbInterface, Context context){
        this.cbInterface = cbInterface;
        this.requests = new ArrayList<>();
        this.offlineRequests = new ArrayList<>();
        this.context = context;
    }

    /**
     * Updates a request in the elasticsearch database after it has been accepted by the rider and turned into ride
     *
     * @param request
     */
    public void accept(Request request){
        request.setAccepted(Boolean.TRUE);
        AsyncController controller = new AsyncController();
        String requestId = request.getID().toString();
        try{
            controller.create("request", requestId, request.toJsonString());
        } catch (Exception e){
            Log.i("Error accepting req", e.toString());
        }
    }

    /** adds the name of the driver to the list of possible drivers
     *  when the driver accepts a request
     *
     * @param request the request the driver is accepting
     * @param driverName the info stored to know who has accepted the request
     */

    public void addDriverToList(Request request, String driverName){
        if(isConnected()) {
            request.addAccepted(driverName);
            AsyncController controller = new AsyncController();
            String requestId = request.getID().toString();
            try {
                controller.create("request", requestId, request.toJsonString());
            } catch (Exception e) {
                Log.i("Error updating driver", e.toString());
            }
        } else {
            offlineSingleton.addDriverAcceptance(request);
        }
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
    public void createRequest(Rider rider, String pickup, String dropoff,LatLng pickupCoords, LatLng dropOffCoords, Date date, float fare, float costDistance){
        AsyncController controller = new AsyncController();
        currenRequest = new Request(rider.getName(), pickup, dropoff, pickupCoords, dropOffCoords, date);
        currenRequest.setFare(fare);
        currenRequest.setCostDistance(costDistance);
        rider.setRequests(new ArrayList<Request>());
        //commented for now so that we can actually create the request without breaking
        //rider.addRequest(currenRequest);
        this.add(currenRequest);

        try{
            if(isConnected()) {
                controller.create("request", currenRequest.getID().toString(), currenRequest.toJsonString());
            } else {
                offlineSingleton.addRiderRequest(currenRequest);
                Toast.makeText(context, "No internet connectivity, request will be sent once online", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Log.i("Error creating request", e.toString());
        }
//        try{
//            controller.create("request", currenRequest.getID().toString(), currenRequest.toJsonString());
//        } catch (Exception e){
//            Log.i("Error creating request", e.toString());
//        }
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
                    //System.out.println(request);
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
            Log.i("doesContain", jsonElement.toString());
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


    public ArrayList<String> getPossibleDrivers(String requestId) {
        AsyncController con = new AsyncController();
        try {
            JsonObject requestJson = con.get("request", "id" , requestId).getAsJsonObject();
            Request request = new Request(requestJson);
            ArrayList<String> drivers = request.getPossibleDrivers();
            return(drivers);
        } catch (Exception e) {
            Log.i("Error parsing requests", e.toString());
        }
        return (null);
    }

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
        requests.clear();
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
        if(isConnected()) {
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
        } else {
            getAllRequests();
        }
        cbInterface.update();

    }

    public void findAllRequestsWithDataMember(String dataType, String variable, String variableValue){
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getFromIndexObjectInArray(dataType, variable, variableValue);

        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        cbInterface.update();
    }
    public void driverAcceptRequest(Request request, String driverName, Rider rider) {
        rider.setPendingNotification("A driver is willing to " +
                "fulfill your Ride! Check your Requests for more info.");
        if(isConnected()) {
            request.addAccepted(driverName);
            AsyncController controller = new AsyncController();
            String requestId = request.getID().toString();
            try {
                controller.create("request", requestId, request.toJsonString());
                controller.create("user", rider.getID().toString(), new Gson().toJson(rider));
                //successful account creation
                Toast.makeText(context, "You have agreed to fulfill a riders request! " +
                        "Wait to see if you're chosen as a driver.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.i("Error updating driver", e.toString());
            }
        } else {
            offlineSingleton.addDriverAcceptance(request);
        }
    }

    public void executeAllPending(String driverName) {
        try {
            //For offline functionality if went online and started this view send pending acceptance of requests
            if (isPendingExecutableAcceptance() && driverName != null) {
                executePendingAcceptance(driverName);
                Toast.makeText(context, "Now online, pending acceptance of request sent", Toast.LENGTH_SHORT).show();
            }

            //Sending requests made when offline if go online
            if (isPendingExecutableRequests()) {
                executePendingRequests();
                Toast.makeText(context, "Now online, pending requests sent", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e) {
            Log.i("Error executing pending", e.toString());
        }
    }

    public void executePendingRequests() {
        AsyncController controller = new AsyncController(this.context);
        for(Request r: offlineSingleton.getRiderRequests()) {
            controller.create("request", r.getID().toString(), r.toJsonString());
        }
        offlineSingleton.clearRiderRequests();
    }

    public void executePendingAcceptance(String driver) {
        for(Request r: offlineSingleton.getDriverRequests()) {
            addDriverToList(r, driver);
        }
        offlineSingleton.clearDriverRequests();
    }

    public boolean isPendingExecutableRequests() {
        return offlineSingleton.getRiderRequests().size() > 0 && isConnected();
    }

    public boolean isPendingExecutableAcceptance() {
        return offlineSingleton.isPendingAcceptance() && isConnected();
    }

    public boolean isPendingExecutableNotification() {
        return offlineSingleton.isPendingNotification() && isConnected();
    }

    private Boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
