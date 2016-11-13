package ca.ualberta.ridr;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

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

        //user named testy must not be in database before tests!!**
        Date date1 = new Date();
        User user = new User("testy", date1, "321", "goodemail", "9999999");

        //add user
        AsyncController controller = new AsyncController();
        try{
            controller.create("user", user.getID().toString(), new Gson().toJson(user));
        } catch (Exception e){
            Log.i("Communication Error", "Could not communicate with the elastic search server");
            assertTrue(false);
        }

        //get user by name
        User onlineUser = null;
        try{
            onlineUser = new Gson().fromJson(controller.get("user", "name", user.getName()), User.class);
            if(onlineUser == null){
                //if we didn't find our user
                Log.i("Communication Error", "Could not communicate with the elastic search server");
                assertTrue(false);
            }
        } catch (Exception e){
            Log.i("Communication Error", "Could not communicate with the elastic search server");
            assertTrue(false);
        }

        assertTrue(user.equals(onlineUser));
        assertEquals(user.getDateOfBirth(), onlineUser.getDateOfBirth());
        assertEquals(user.getID().toString(), onlineUser.getID().toString());
        assertEquals(user.getEmail(), onlineUser.getEmail());
        assertEquals(user.getPhoneNumber(), onlineUser.getPhoneNumber());
        assertEquals(user.getCreditCard(), onlineUser.getCreditCard());

        //get user by UUID
        onlineUser = null;
        try{
            onlineUser = new Gson().fromJson(controller.get("user", "id", user.getID().toString()), User.class);
            if(onlineUser == null){
                //if we didn't find our user
                Log.i("Communication Error", "Could not communicate with the elastic search server");
                assertTrue(false);
            }
        } catch (Exception e){
            Log.i("Communication Error", "Could not communicate with the elastic search server");
            assertTrue(false);
        }

        assertTrue(user.equals(onlineUser));
        assertEquals(user.getDateOfBirth(), onlineUser.getDateOfBirth());
        assertEquals(user.getID().toString(), onlineUser.getID().toString());
        assertEquals(user.getEmail(), onlineUser.getEmail());
        assertEquals(user.getPhoneNumber(), onlineUser.getPhoneNumber());
        assertEquals(user.getCreditCard(), onlineUser.getCreditCard());


        //delete our user off of the database
        AsyncDatabaseController databaseController = new AsyncDatabaseController("deleteUserTests");
        databaseController.execute(user.getID().toString());
    }
}
