package ca.ualberta.ridr;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Test;
/*
import static org.mockito.Mockito.*;
*/

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Marco on 27-Nov-2016.
 */

public class EditProfileTest {

    /*
    @Test
    public void changeInfoTest(){
        Driver driver = new Driver("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "1 780-555-1122");
        driver.setVehicleDescription("some car");

        //Create mock controller
        //DriverController mockDriverController = mock(DriverController.class);
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to the mock controller
        mockedController.create("user", driver.getID().toString(), new Gson().toJson(driver));

        //retrieve driver
        Driver editDriver = new Gson().fromJson(mockedController.get("user", "name", driver.getName()), Driver.class);

        editDriver.setEmail("newEmail@ualberta.ca");
        editDriver.setPhoneNumber("1 780-555-1234");
        editDriver.setVehicleDescription("some other car");

        //send new info to mock controller
        mockedController.create("user", driver.getID().toString(), new Gson().toJson(editDriver));

        //test edited info
        verify(mockedController).create("user", driver.getID().toString(), new Gson().toJson(editDriver));

        //retrieve edited info

        Driver testDriver = new Gson().fromJson(mockedController.get("user", "name", driver.getName()), Driver.class);

        //test that info has been updated
        assertEquals(testDriver.getEmail(), "newEmail@ualberta.ca");
        assertEquals(testDriver.getPhoneNumber(), "1 780-555-1234");
        assertEquals(testDriver.getVehicleDescription(), "some other car");
        assertNotEquals(testDriver.getEmail(), "jbarclay@ualberta.ca");
        assertNotEquals(testDriver.getPhoneNumber(), "1 780-555-1122");
        assertNotEquals(testDriver.getVehicleDescription(), "some car");

    }
    */
}
