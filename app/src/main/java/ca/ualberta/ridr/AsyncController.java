package ca.ualberta.ridr;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by Justin on 2016-11-10.
 *
 * This class can get and put objects into elastic search, in all different elastic search types.
 * Controls all elastic search interactions.
 */
public class AsyncController {
    /**
     * The Controller.
     */
    AsyncDatabaseController controller;

    /**
     * Instantiates a new Async controller.
     */
    public AsyncController(){
        super();
    }

    /**
     * Gets a single object from the database and returns it as a JsonObject.
     *
     * @param dataClass the mapping of the object
     * @param attribute the attribute
     * @param value     the value
     * @return the json object
     */
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

    /**
     * Gets all items from a mapping in the databases
     *
     * @param dataClass the mapping of the object
     * @return an array of jsonObjects from that mapping
     */
    public JsonArray getAllFromIndex(String dataClass) {
        controller = new AsyncDatabaseController("getAllFromIndex");
        try{
            String searchString = "{\"query\": { \"match_all\": { }}}";

            return extractAllElements(controller.execute(dataClass, searchString).get());
        } catch(Exception e){
            return null;
        }
    }

    /**
     * Gets all items from a particular mapping of the database that matches an attribute
     *
     * @param dataClass     the mapping of the object
     * @param variable      the variable to match
     * @param variableValue the variable value
     * @return a json array of that object
     */
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

    /**
     * Create or update an object in the databases
     *
     * @param dataClass  the mapping into the index
     * @param id         the id of the objects
     * @param jsonObject A stringified version of the object
     * @return the result of the query
     */
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

    /**
     * Builds up a query and returns 0 or more elements
     * If an exception occurs return null
     * @param dataClass  the data class
     * @param center     the center
     * @param kmDistance the km distance
     * @return a jsonArray
     */
    public JsonArray geoDistanceQuery(String dataClass,LatLng center, String kmDistance){
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

    public JsonArray getFromIndexObjectInArray(String dataClass, String variable, String variableValue){
        controller = new AsyncDatabaseController("get");
        String query =
                "{"+
                        "\"query\": {" +
                        "\"bool\" : {"+
                        "\"should\": [" +
                        "{ \"match\": { \"" + variable + "\": \"" + variableValue + "\" } }" +
                                    "]" +
                                "}" +
                            "}"+
                        "}";
        try{
            return extractAllElements(controller.execute(dataClass, query).get());
        } catch(Exception e){
            Log.d("Elastic search filter", "getFromIndexObjectInArray: " + e.toString());
            return null;
        }
    }

    /**
     * A simple function to extract the jsonArray from the results
     * @param result  the result of a query
     * @return a jsonArray
     */
    private JsonArray extractAllElements(JsonObject result){
        return result.getAsJsonObject("hits").getAsJsonArray("hits");
    }

    /**
     * A simple function to extract the jsonObject from the result
     * @param result  the result of a query
     * @return a jsonObject
     */
    private JsonObject extractFirstElement(JsonObject result){
        return result.getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source");
    }

}
