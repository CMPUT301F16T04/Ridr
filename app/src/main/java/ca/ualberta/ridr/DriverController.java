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
    ACallback cbInterface;
    Driver currentDriver;
    AsyncController controller;
    DriverController(){
    }

    DriverController(ACallback cbInterface){
        this.cbInterface = cbInterface;
    }

    public Driver getDriverFromServer(String driverId){
        Driver driver = new Gson().fromJson(new AsyncController().get("user", "id", driverId), Driver.class);
        return(driver);
    }

}
