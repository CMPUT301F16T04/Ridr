package ca.ualberta.ridr;


import com.google.gson.Gson;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

    private Context context;
    private OfflineSingleton offlineSingleton = OfflineSingleton.getInstance();

    RiderController(Context context){
        this.context = context;
    }

    public Rider getRiderFromServer(String riderId) {
        Rider rider = new Gson().fromJson(new AsyncController(context).get("user", "id", riderId), Rider.class);
        return rider;
    }

    public Rider getRiderFromServerUsingId(String riderId){
        Rider rider = new Gson().fromJson(new AsyncController(context).get("user", "id", riderId), Rider.class);
        return(rider);
    }

    public Rider getRiderFromServerUsingName(String riderName){
        Rider rider = new Gson().fromJson(new AsyncController(context).get("user", "name", riderName), Rider.class);
        return(rider);
    }
    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

    public void setPendingNotification(Rider rider) {
            rider.setPendingNotification("A driver is willing to " +
                    "fulfill your Ride! Check your Requests for more info.");
            try {
                AsyncController asyncController = new AsyncController(context);
                asyncController.create("user", rider.getID().toString(), new Gson().toJson(rider));

            } catch (Exception e) {
                Log.i("Communication Error", "Could not communicate with the elastic search server");
                return;
            }
    }

    public void pushPendingNotifications() {
        if (offlineSingleton.getRiderList().size() > 0 && isConnected()) {
            for (Rider rider : offlineSingleton.getRiderList()) {
                setPendingNotification(rider);
            }
            offlineSingleton.clearRiderList();
        }
    }

    private Boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

