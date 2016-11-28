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
 *
 * This controller can save and get Rider users from our server, using the AsyncController.
 */
public class RiderController {

    /**
     * Instantiates a new Rider controller.
     */
    RiderController(){
    }

    /**
     * Gets rider from server.
     *
     * @param riderName the rider name
     * @return the rider from server
     */
    public Rider getRiderFromServer(String riderName) {
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "name", riderName), Rider.class);
        return (rider);
    }

    /**
     * Get rider from server using id rider.
     *
     * @param riderId the rider id
     * @return the rider
     */
    public Rider getRiderFromServerUsingId(String riderId){
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "id", riderId), Rider.class);
        return(rider);
    }

    /**
     * Get rider from server using name of rider.
     *
     * @param riderName the rider name
     * @return the rider
     */
    public Rider getRiderFromServerUsingName(String riderName){
        Rider rider = new Gson().fromJson(new AsyncController().get("user", "name", riderName), Rider.class);
        return(rider);
    }

    /**
     * Get requests array list.
     *
     * @param rider the rider
     * @return the array list
     */
    public ArrayList<Request> getRequests(Rider rider){
        return(rider.getRequests());
    }

    /**
     * Save changes to our server.
     *
     * @param riderName the rider name
     * @param phone     the phone
     * @param email     the email
     */
    public void saveChanges(String riderName, String phone, String email){
        Rider rider = getRiderFromServerUsingName(riderName);
        rider.setPhoneNumber(phone);
        rider.setEmail(email);
        AsyncController controller = new AsyncController();
        controller.create("user", rider.getID().toString(), new Gson().toJson(rider));
    }

}

