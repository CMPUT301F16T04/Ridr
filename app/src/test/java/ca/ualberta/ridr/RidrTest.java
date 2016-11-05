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
        //Initialize classes for the test
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request_copy = new Request(rider, "University of Alberta", " West Edmonton Mall");

        //Create controller for rider
        RiderController riderController = new RiderController(rider);
        //Associate requests with their rider
        riderController.addRequest("University of Alberta", " West Edmonton Mall");
        riderController.addRequest("Rogers Place", "Whyte Ave");

        //Accept the second ride
        Request request_accepted = new Request(rider, "Rogers Place", "Whyte Ave");
        riderController.acceptRequest(request_accepted);

        //Check that the openRequests list is of the right size, and contains the correct item
        ArrayList<Request> openRequests = riderController.getOpenRequests();
        Log.v(TAG, "index=" + openRequests.size());
        assertEquals(openRequests.size(), 1);
    }
}
