package ca.ualberta.ridr;


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
    private JsonArray jsonArray;
    private ArrayList<Request> requests;
    private ACallback cbInterface;

    public RequestController(){}

    /**
     * Uses a keyword to search through the queryable fields for requests
     * Returns the requests containing the keyword in one or more of the fields
     * Does not return duplicates of the same request if multiple instances of keyword
     * @param keyword
     * @return ArrayList<Request>
     */
    public ArrayList<Request> searchRequestsKeyword(String keyword) {
        jsonArray = new AsyncController().getAllFromIndex("request");
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
