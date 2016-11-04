package ca.ualberta.ridr;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

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
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.ridr", appContext.getPackageName());
    }

    @Test
    // Test for Rider Profile US 03.01.01
    public void testUserProfile(){
        //this is bad, rewrite
        //instead, create a User, upload the rider and driver to elasticsearch using their controllers
        //then get it from elasticsearch, also using the controllers.

        Date date1 = new Date();
        Rider user1 = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Date date2 = new Date();
        Driver user2 = new Driver("Storm", new Date(), "123", "goodemail@supergood.com", "6666666", null); //null for no vehicle assigned yet

        DriverController driverController = new DriverController();
        RiderController riderController = new RiderController();

        driverController.AddDriverTaskTest(user2);
        riderController.AddRiderTaskTest(user1);

        Driver newDriver = driverController.GetDriverTaskTest("Storm");
        Rider newRider  = riderController.GetRiderTaskTest("Storm");

        /* Code for Async tests, has to be tested in android emulator
        Driver newDriver = null;
        try{
            newDriver = getDriverTask.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the driver out of the async object.");
        }*/

        /* Code for Async tests, has to be tested in android emulator
        getRiderTask.execute("Steve");
        Rider newRider = null;
        try{
            newRider = getRiderTask.get();
        } catch (Exception e){
            Log.i("Error", "Failed to get the rider out of the async object.");
        }*/

        //check first User, who is logged in as a rider
        assertEquals("Steve", newRider.getName());
        assertEquals(date1, newRider.getDateOfBirth());
        assertEquals("321", newRider.getCreditCard());

        //check second User, who is logged in as a driver
        assertEquals("Storm", newDriver.getName());
        assertEquals(date2, newDriver.getDateOfBirth());
        assertEquals("123", newDriver.getCreditCard());
        assertEquals(null, newDriver.getVehicle());

    }
}
