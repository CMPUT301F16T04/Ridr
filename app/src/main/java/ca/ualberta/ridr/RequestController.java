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
    private ACallback cbInterface;

    public RequestController(ACallback cbInterface){
        this.cbInterface = cbInterface;
        this.requests = new ArrayList<>();
    }

    public int size(){
        return requests.size();
    }

    public ArrayList<Request> getList(){
        return requests;
    }
    public void getUserRequest(final UUID userID) {
        // Get all user requests from the database
        Thread getUser = new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncController controller = new AsyncController();
                System.out.println("Adding request");
                JsonArray queryResults = controller.getAllFromIndex("request");
                Log.i("Rider", queryResults.toString());
                for (JsonElement result : queryResults) {
                    try {
                        System.out.println("Adding request");
                        requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
                    } catch (Exception e) {
                        Log.i("Error parsing requests", e.toString());
                    }
                }
                cbInterface.callback();
            }
        });
        getUser.start();
    }
}
