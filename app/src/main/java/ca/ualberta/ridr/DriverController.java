package ca.ualberta.ridr;

import com.google.gson.Gson;

/**
 * Created by jferris on 22/10/16.
 */
public class DriverController {
    DriverController(){
    }
    public Driver getDriverFromServer(String driverId){
        Driver driver = new Gson().fromJson(new AsyncController().get("user", "id", driverId), Driver.class);
        return(driver);
    }
}
