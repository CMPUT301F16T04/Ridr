package ca.ualberta.ridr;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddUserProfileTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.ridr", appContext.getPackageName());
    }

    @Test
    // Test for Rider Profile US 03.01.01
    public void testUserProfile(){

        Date date1 = new Date();
        Rider user1 = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Date date2 = new Date();
        Driver user2 = new Driver("Storm", new Date(), "123", "goodemail@supergood.com", "6666666", null); //null for no vehicle assigned yet

        DriverController.AddDriverTaskTest addDriverTask = new DriverController.AddDriverTaskTest();
        RiderController.AddRiderTaskTest addRiderTask = new RiderController.AddRiderTaskTest();

        addDriverTask.execute(user2);
        addRiderTask.execute(user1);

        DriverController.GetDriverTaskTest getDriverTask = new DriverController.GetDriverTaskTest();
        RiderController.GetRiderTaskTest getRiderTask = new RiderController.GetRiderTaskTest();

        //Code for Async tests, has to be tested in android emulator
        getDriverTask.execute("Storm");
        Driver newDriver = null;
        try{
            newDriver = getDriverTask.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the driver out of the async object.");
        }

        getRiderTask.execute("Steve");
        Rider newRider = null;
        try{
            newRider = getRiderTask.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the rider out of the async object.");
        }

        //check first User, who is logged in as a rider
        assertEquals("Steve", newRider.getName());
        //assertEquals(date1, newRider.getDateOfBirth());
        assertEquals("321", newRider.getCreditCard());

        //check second User, who is logged in as a driver
        assertEquals("Storm", newDriver.getName());
        //assertEquals(date2, newDriver.getDateOfBirth());
        assertEquals("123", newDriver.getCreditCard());
        assertEquals(null, newDriver.getVehicle());

    }
}
