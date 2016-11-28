package ca.ualberta.ridr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
    Context context;

    /**
     * Instantiates a new Async controller.
     */
    public AsyncController(Context context) {
        super();
        this.context = context;
    }

    public AsyncController() {};

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
        String file = getFile(dataClass, attribute);
        if(isConnected()) {
            controller = new AsyncDatabaseController("get");
            try {
                String searchString = "{\"query\": { \"bool\": { \"must\": { \"match\": { \"" + attribute + "\":\"" + value + "\"}}}}}";
                JsonArray jsonArray = extractAllElements(controller.execute(dataClass, searchString).get());
                return jsonArray.get(0).getAsJsonObject().getAsJsonObject("_source");
            } catch (Exception e) {
                return null;
            }
        } else {
            return searchInFileObject(dataClass, attribute, value);
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
        String file = getFile(dataClass, "");
        try{
            if(isConnected()) {
                String searchString = "{\"query\": { \"match_all\": { }}}";

                JsonArray jArray = extractAllElements(controller.execute(dataClass, searchString).get());
                saveInFile(jArray, file);
                return jArray;
            } else {
                return loadFromFile(file);
            }

        } catch(Exception e) {
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
        String file = getFile(dataClass, variable);
        try{
            if(isConnected()) {
                String searchString = "{\"query\": { \"multi_match\": { \"query\": \"" + variableValue + "\", " +
                        "fields: [ \"" + variable + "\"]}}}";

                JsonArray jArray = extractAllElements(controller.execute(dataClass, searchString).get());
                saveInFile(jArray, file);
                return jArray;
            } else {
                return loadFromFile(file);
            }
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
        String file = getFile(dataClass, "");
        try{
            if(isConnected()) {
                JsonArray jsonArray = extractAllElements(controller.execute(dataClass, query).get());
                saveInFile(jsonArray, file);
                return jsonArray;
            } else {
                return loadFromFile(file);
            }
        } catch(Exception e){
            Log.d("Elastic search filter", e.toString());
            return null;
        }
    }

    public JsonArray getFromIndexObjectInArray(String dataClass, String variable, String variableValue){
        controller = new AsyncDatabaseController("get");
        //got help with query from http://www.tugberkugurlu.com/archive/elasticsearch-array-contains-search-with-terms-filter -Tugberk Ugurlu
        String query =
                "{"+
                    "\"query\": {" +
                        "\"filtered\" : {"+
                            "\"query\": {" +
                                "\"match_all\": {}" +
                            "}," +
                            "\"filter\": {" +
                                "\"terms\": {" +
                                    "\"" + variable + "\": [\"" + variableValue + "\"]" +
                                "}" +
                            "}" +
                        "}" +
                    "}"+
                "}";

        String file = getFile(dataClass, variable);
        try{
            if(isConnected()) {
                JsonArray jsonArray = extractAllElements(controller.execute(dataClass, query).get());
                saveInFile(jsonArray, file);
                return jsonArray;
            } else {
                return loadFromFile(file);
            }
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
    private JsonObject extractFirstElement(JsonObject result) {
        return result.getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source");
    }

    /**
     * loads a json array from the given file
     * @param file JsonArray
     * @return
     */
    private JsonArray loadFromFile(String file) {
        JsonArray jsonArray;
        try {
            FileInputStream fis = context.openFileInput(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Code from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            Type type = new TypeToken<JsonArray>(){}.getType();

            jsonArray = gson.fromJson(in, type);
            fis.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            jsonArray = new JsonArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        return jsonArray;
    }

    /**
     * Saves the jsonArray into a file
     * @param jsonArray
     * @param file
     */
    private void saveInFile(JsonArray jsonArray, String file) {
        try {
            FileOutputStream fos = context.openFileOutput(file,
                    0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(jsonArray, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("ioerror", e.toString());
        }
    }

    /**
     * Returns a file name used to make a file to save the data found from
     * dataclass and variable
     * @param dataClass
     * @param variable
     * @return String
     */
    private String getFile(String dataClass, String variable) {
        String file = dataClass + variable + ".sav";
        return file;
    }

    /**
     * Check if connected to the internet
     * @return Boolean
     */
    private Boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * Searches a file for the field attribute with data that matches keyword
     * @param dataClass
     * @param attribute
     * @param keyword
     * @return JsonObject
     */
    private JsonObject searchInFileObject(String dataClass, String attribute, String keyword) {
        String file = getFile(dataClass, "");
        JsonArray jsonArray = loadFromFile(file);
        JsonObject jsonObject;
        JsonElement temp;
        String string;
        keyword = keyword.trim();
        keyword = "cb301ddf-483e-49fa-ba43-aab1ee4e1098";

        for(JsonElement jsonElement: jsonArray) {
            jsonObject = jsonElement.getAsJsonObject();
            temp = jsonObject.get("_" + attribute);
            string = temp.getAsString();
            if(temp.equals(keyword)) {
                return jsonObject;
            }
        }
        return null;
    }
}
