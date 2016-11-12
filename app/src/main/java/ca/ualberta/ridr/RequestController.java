package ca.ualberta.ridr;

import android.util.Log;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by jferris on 22/10/16.
 */
public class RequestController extends Observable {
    private ArrayList<Request> requests;


    public RequestController(){
        this.requests = new ArrayList<>();
    }

    public void addAllRequest(UUID userID){
        AsyncController controller = new AsyncController();

        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", userID.toString());
        Log.i("Rider", queryResults.toString());
        for(JsonElement result : queryResults){
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
                notify();
            } catch (Exception e){
                Log.i("Error parsing requests", e.toString());
            }
        }
    }
}
