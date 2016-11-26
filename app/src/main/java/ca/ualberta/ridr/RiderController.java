package ca.ualberta.ridr;


import com.google.gson.Gson;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;


import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


/**
 * Created by jferris on 22/10/16.
 */
public class RiderController {

    RiderController(){
    }
    public Rider getRiderFromServerUsingId(String riderId){
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "id", riderId), Rider.class);
        return(rider);
    }

    public Rider getRiderFromServerUsingName(String riderName){
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "name", riderName), Rider.class);
        return(rider);
    }
    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

}

