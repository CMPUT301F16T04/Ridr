package ca.ualberta.ridr;

import android.os.AsyncTask;

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
     * Get json object from elastic search.
     *
     * @param dataClass the data class we are viewing in elastic search
     * @param attribute the attribute we are searching for
     * @param value     the value of the attribute that we want
     * @return the json object from elastic search
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
     * Gets all objects from index.
     *
     * @param dataClass the data class we are viewing in elastic search
     * @return the Json Array from index
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
     * Gets all objects from index, filtered.
     *
     * @param dataClass     the data class we are viewing in elastic search
     * @param variable      the variable we want to filter
     * @param variableValue the variable value we want to filter
     * @return the json array from the index, filtered
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
     * Create json object, and put it in elastic search
     *
     * @param type       the type we are viewing in elastic search
     * @param id         the id of the object we are putting in elastic search
     * @param jsonObject the json object that we are putting in elastic search
     * @return the json object
     */
    public JsonObject create(String type, String id, String jsonObject){
        // Pass a jsonified object and have it stored on elasticsearch
        controller = new AsyncDatabaseController("create");
        try{
            return controller.execute(type, id, jsonObject).get();
        } catch(Exception e){
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