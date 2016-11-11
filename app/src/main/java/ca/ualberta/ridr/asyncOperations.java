package ca.ualberta.ridr;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by Justin on 2016-11-10.
 */

public class asyncOperations {
    AsyncDatabaseController controller;

    public asyncOperations(){
        super();
    }

    public JsonObject get(String dataClass, String type, String value) {
        controller = new AsyncDatabaseController("get");
        try{
            String searchString = "{\"query\": { \"bool\": { \"must\": { \"match\": { \""+ type+"\":\"" + value + "\"}}}}}";

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

    /*public JsonObject getAllFromIndexFiltered(String dataClass, String index, String variable, String variableValue ) {
        controller = new AsyncDatabaseController("getAllFromIndexFiltered");
        try{
            String searchString = "{\"query\": { \"bool\": { \"must\": { \"match\": { \""+ type+"\":\"" + value + "\"}}}}}";

            return controller.execute(dataClass, searchString).get();
        } catch(Exception e){
            return null;
        }
    }*/


    public JsonObject create(String type, String jsonObject){
        controller = new AsyncDatabaseController("create");
        try{
            return controller.execute(type, jsonObject).get();
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