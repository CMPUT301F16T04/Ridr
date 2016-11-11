package ca.ualberta.ridr;

import android.os.AsyncTask;

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

            return controller.execute(dataClass, searchString).get();
        } catch(Exception e){
            return null;
        }
    }

    public JsonObject create(String type, String jsonObject){
        controller = new AsyncDatabaseController("create");
        try{
           // String searchString = "{\"query\": { \"bool\": { \"must\": { \"match\": { \""+ type+"\":\"" +  + "\"}}}}}";

            return controller.execute(type, jsonObject).get();
        } catch(Exception e){
            return null;
        }
    }

    // How the fuck do we update or create?
    public static boolean update(){
        return true;
    }
}