package ca.ualberta.ridr;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mackenzie on 12/10/16.
 */
public class RidrTest{
    // Test for retrieving current ridr's open requests US 01.02.01*/
    private static final String TAG = "MyActivity";

    @Test
    public void testGetOpenRequests(){
        //NOTE: Does not use request MVC
        //Initialize classes for the test
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");

        //Create controller for rider
        RiderController riderController = new RiderController(rider);
        //Associate requests with their rider
        Request req1 = new Request(rider,"University of Alberta", " West Edmonton Mall");
        Request req2 = new Request(rider, "Rogers Place", "Whyte Ave");
        req2.setAccepted(true);

        riderController.addRequest(req1);
        riderController.addRequest(req2);

        //Check that the openRequests list is of the right size, and contains the correct item
        ArrayList<Request> openRequests = riderController.getOpenRequests();

        assertTrue(openRequests.size() == 1);
    }


}
