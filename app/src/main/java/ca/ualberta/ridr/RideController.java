package ca.ualberta.ridr;

import java.util.Date;

/**
 * Created by jferris on 22/10/16.
 */
public class RideController {
    RideController(){}


    public void confirmDriver(Driver driver, Request request, Rider rider) {
        rider.confirmDriver(driver, request);
    }
}
