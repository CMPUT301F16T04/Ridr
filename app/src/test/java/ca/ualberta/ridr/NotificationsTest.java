package ca.ualberta.ridr;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Storm on 2016-11-27.
 */

public class NotificationsTest {
    /**
     * Tests US 1.03.01 and US 5.04.01
     */

    @Test
    public void pendingNotificationDriverTest(){
        Driver driver = new Driver("John", new Date(), "1111222233334444", "123@hotmail.com", "780 450 1111");
        //save pendingNotification for driver
        driver.setPendingNotification("You have been chosen as a Driver for a Ride! View Rides " +
                "for more info.");

        String jsonDriver = new Gson().toJson(driver);

        //maybe upload to elastic search as part of test?

        Driver newDriver = new Gson().fromJson(jsonDriver, Driver.class);

        assertTrue(newDriver.getPendingNotification() != null);
        assertTrue(newDriver.getPendingNotification().equals(driver.getPendingNotification()));
    }

    @Test
    public void pendingNotificationRiderTest(){
        Rider rider = new Rider("John", new Date(), "1111222233334444", "123@hotmail.com", "780 450 1111");
        //save pendingNotification for driver
        rider.setPendingNotification("A driver is willing to " +
                "fulfill your Ride! Check your Requests for more info.");

        String jsonRider = new Gson().toJson(rider);

        //maybe upload to elastic search as part of test?

        Rider newRider = new Gson().fromJson(jsonRider, Rider.class);

        assertTrue(newRider.getPendingNotification() != null);
        assertTrue(newRider.getPendingNotification().equals(rider.getPendingNotification()));
    }

}
