package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
* Created by Justin on 2016-11-10.
*/

public class AsyncController {
    AsyncDatabaseController controller;

    public AsyncController(){
        super();
    }

    public JsonObject get(String dataClass, String attribute, String value) {
        // This takes an objectType, attribute, and value and returns the first match that elastic
        // search finds
        controller = new AsyncDatabaseController("get");
        try{
            String searchString = "{\"query\": { \"bool\": { \"must\": { \"match\": { \""+ attribute+"\":\"" + value + "\"}}}}}";

            return extractFirstElement(controller.execute(dataClass, searchString).get());
        } catch(Exception e){
            return null;
        }
    }

    public JsonArray getAllFromIndex(String dataClass) {
        controller = new AsyncDatabaseController("getAllFromIndex");
        try{
            String searchString = "{\"query\": { \"match_all\": { }}}";

            return extractAllElements(controller.execute(dataClass, searchString).get());
        } catch(Exception e){
            return null;
        }
    }

    public JsonArray getAllFromIndexFiltered(String dataClass, String variable, String variableValue ) {
        controller = new AsyncDatabaseController("get");
        try{
            String searchString = "{\"query\": { \"multi_match\": { \"query\": \"" + variableValue + "\", " +
                    "fields: [ \"" + variable + "\"]}}}";

            return extractAllElements(controller.execute(dataClass, searchString).get());
        } catch(Exception e){
            return null;
        }
    }


    public JsonObject create(String dataClass,String id, String jsonObject){
        // Pass a jsonified object and have it stored on elasticsearch
        controller = new AsyncDatabaseController("create");
        try{
            return controller.execute(dataClass, id, jsonObject).get();
        } catch(Exception e){
            Log.d("Elastic Search Creation", e.toString());
            return null;
        }
    }

    public JsonArray geoDistanceQuery(final String dataClass,final LatLng center, final String kmDistance){
        controller = new AsyncDatabaseController("get");
        String query =
                "{"+
                    "\"query\": {" +
                        "\"bool\" : {"+
                            "\"must\" : {"+
                                "\"match_all\" : {}" +
                            "}," +
                            "\"filter\" : {"+
                                "\"geo_distance\" : {" +
                                    "\"distance\" : \""+ kmDistance+ "km\"," +
                                    "\"pickupCoord\" : {" +
                                            "\"lat\" :" + center.latitude + "," +
                                            "\"lon\" :" + center.longitude +
                                    "}" +
                                "}" +
                            "}" +
                        "}" +
                    "}"+
                "}";
        try{
            return extractAllElements(controller.execute(dataClass, query).get());
        } catch(Exception e){
            Log.d("Elastic search filter", e.toString());
            return null;
        }
    }

    private JsonArray extractAllElements(JsonObject result){
        return result.getAsJsonObject("hits").getAsJsonArray("hits");

    }
    private JsonObject extractFirstElement(JsonObject result){
        return result.getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source");
    }

}