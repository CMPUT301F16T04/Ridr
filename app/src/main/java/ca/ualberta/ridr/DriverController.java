package ca.ualberta.ridr;


import com.google.gson.Gson;
import android.content.Context;
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
public class DriverController {


    DriverController(){}

    //is this supposed to be a driver controller item or a request controller item... look at UML in the morning
    public void acceptRequest(Driver driver, Request request){
        driver.acceptRequest(request);
    }

    public Driver getDriverFromServerUsingId(String driverId){
        Driver driver = new Gson().fromJson(new AsyncController().get("user", "id", driverId), Driver.class);
        return(driver);
    }

    public Driver getDriverFromServerUsingName(String driverName){
        Driver driver = new Gson().fromJson(new AsyncController().get("user", "name", driverName), Driver.class);
        return(driver);
    }

    public void saveChanges(String driverName, String phone, String email, String vehicle){
        Driver driver = getDriverFromServerUsingName(driverName);
        driver.setPhoneNumber(phone);
        driver.setEmail(email);
        driver.setVehicleDescription(vehicle);
        AsyncController controller = new AsyncController();
        controller.create("user", driver.getName(), new Gson().toJson(driver));
    }

}
