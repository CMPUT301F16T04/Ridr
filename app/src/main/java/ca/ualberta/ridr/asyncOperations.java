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

    public JsonObject get(String type, String id) {
        controller = new AsyncDatabaseController();
        try{
            return controller.execute(type, id).get();
        } catch(Exception e){
            return null;
        }
    }

    // How the fuck do we update or create?
    public static boolean update(){
        return true;
    }
}