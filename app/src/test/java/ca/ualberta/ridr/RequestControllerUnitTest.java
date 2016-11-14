package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Marco on 03-Nov-2016.
 */
public class RequestControllerUnitTest {

    @Test
    // needs to be updated since changes were made to the controller
    public void testCreateRequestController(){
        Date date = new Date();
        Rider rider = new Rider("Bob", date, "123", "email", "phone"); // fails if rider = null
        RequestController RC = new RequestController();
        String start = "start";
        String end = "end";
        LatLng a = new LatLng(1,2);
        LatLng b = new LatLng(2,3);
        RC.createRequest(rider, start, end, a, b, date);
        Request request = new Request(rider.getID().toString(), start, end, a, b, date);
        ArrayList<Request>  riderRequest = rider.getRequests();
        assertTrue(riderRequest.get(0).getPickup() == request.getPickup());
        //still cant use isEquals here because we do create two separate instances of requests in this test
    }
}
