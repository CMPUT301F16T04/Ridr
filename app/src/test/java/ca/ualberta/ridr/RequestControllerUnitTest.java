package ca.ualberta.ridr;

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
    public void createRequestControllerTest(){
        Date date = new Date();
        Rider rider = new Rider("Bob", date, "123", "email", "phone");
        RequestController RC = new RequestController();
        String start = "start";
        String end = "end";
        RC.createRequest(rider, start, end);
        Request request = new Request(rider, start, end);
        ArrayList<Request>  riderRequest = rider.getRequests();
        assertEquals(request, riderRequest.get(0));
    }
}
