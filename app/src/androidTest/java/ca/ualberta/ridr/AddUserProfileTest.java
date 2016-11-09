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
    /**
     * Test for Rider Profile US 03.01.01
     */
    public void testUserProfile(){

        Date date1 = new Date();
        Rider user1 = new Rider("Steve", date1, "321", "goodemail", "9999999");
        Date date2 = new Date();
        Driver user2 = new Driver("Storm", date2, "123", "goodemail@supergood.com", "6666666", null); //null for no vehicle assigned yet

        DriverController.AddDriverTaskTest addDriverTask = new DriverController.AddDriverTaskTest();
        RiderController.AddRiderTaskTest addRiderTask = new RiderController.AddRiderTaskTest();

        //add in the rider and driver
        addDriverTask.execute(user2);
        addRiderTask.execute(user1);
        try{
            Thread.sleep(8000);//wait 8 seconds, for all async tasks to complete
        } catch (Exception e){
            Log.i("Wait Exception", "Your wait got interrupted! Before asserting.");
        }

        //get objects by name
        DriverController.GetDriverByNameTaskTest GetDriverByNameTask = new DriverController.GetDriverByNameTaskTest();
        RiderController.GetRiderByNameTaskTest GetRiderByNameTask = new RiderController.GetRiderByNameTaskTest();

        //Code for Async tests, has to be tested in android emulator
        Driver newDriver = null;
        try{
            newDriver = GetDriverByNameTask.execute("Storm").get();//this will make the main thread wait until
            //done, which is what we want for tests. Not for main program!
        } catch (Exception e){
            Log.i("Error", "Failed to get the driver out of the async object.");
        }
        Rider newRider = null;
        try{
            newRider = GetRiderByNameTask.execute("Steve").get(); //this will make the main thread wait until
            //done, which is what we want for tests. Not for main program!
        } catch (Exception e){
            Log.i("Error", "Failed to get the rider out of the async object.");
        }

        try{
            Thread.sleep(10000);//wait 10 seconds, for all async tasks to complete
        } catch (Exception e){
            Log.i("Wait Exception", "Your wait got interrupted! Before asserting.");
        }

        //check first User, who is logged in as a rider
        assertEquals("Steve", newRider.getName());
        assertEquals("321", newRider.getCreditCard());
        assertEquals(user1.getElasticID(), newRider.getElasticID());

        //check second User, who is logged in as a driver
        assertEquals("Storm", newDriver.getName());
        assertEquals("123", newDriver.getCreditCard());
        assertEquals(null, newDriver.getVehicle());
        assertEquals(user2.getElasticID(), newDriver.getElasticID());

        //get objects by UUID
        DriverController.GetDriverByUUIDTaskTest GetDriverByUUIDTask = new DriverController.GetDriverByUUIDTaskTest();
        RiderController.GetRiderByUUIDTaskTest GetRiderByUUIDTask = new RiderController.GetRiderByUUIDTaskTest();

        //Code for Async tests, has to be tested in android emulator
        newDriver = null;
        try{
            newDriver = GetDriverByUUIDTask.execute(user2.getUUID().toString()).get();//this will make the main thread wait until
            //done, which is what we want for tests. Not for main program!
        } catch (Exception e){
            Log.i("Error", "Failed to get the driver out of the async object.");
        }
        newRider = null;
        try{
            newRider = GetRiderByUUIDTask.execute(user1.getUUID().toString()).get(); //this will make the main thread wait until
            //done, which is what we want for tests. Not for main program!
        } catch (Exception e){
            Log.i("Error", "Failed to get the rider out of the async object.");
        }

        try{
            Thread.sleep(10000);//wait 10 seconds, for all async tasks to complete
        } catch (Exception e){
            Log.i("Wait Exception", "Your wait got interrupted! Before asserting.");
        }

        //check first User, who is logged in as a rider
        assertEquals("Steve", newRider.getName());
        assertEquals("321", newRider.getCreditCard());
        assertEquals(user1.getElasticID(), newRider.getElasticID());

        //check second User, who is logged in as a driver
        assertEquals("Storm", newDriver.getName());
        assertEquals("123", newDriver.getCreditCard());
        assertEquals(null, newDriver.getVehicle());
        assertEquals(user2.getElasticID(), newDriver.getElasticID());


        //delete our objects in test type, so next tests pass
        DriverController DriverControllerClass = new DriverController();
        DriverControllerClass.deleteDriverTests(newDriver.getElasticID());
        RiderController RiderControllerClass = new RiderController();
        RiderControllerClass.deleteRiderTests(newRider.getElasticID());

    }
}
