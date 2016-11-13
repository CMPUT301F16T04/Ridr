package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Justin on 2016-11-11.
 */
    public class AsyncTest {
    @Test
    public void addRequestTest() throws InterruptedException {
//        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
//        Request request = new Request(rider, "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
//        Request request1 = new Request(rider, "10615 47 Avenue Northwest, Edmonton", "West Edmonton Mall",  new LatLng(53.484775, -113.505067), new LatLng(53.5225, -113.6242), new Date() );
//        Request request2 = new Request(rider, "West Edmonton Mall", "10615 47 Avenue Northwest, Edmonton",  new LatLng(53.5225, -113.6242) , new LatLng(53.484775, -113.505067), new Date() );
//
//        AsyncController controller = new AsyncController();
//        controller.create("user", rider.getID().toString(), new Gson().toJson(rider));
//
//        controller.create("request", request.getID().toString(), new Gson().toJson(request));
//        controller.create("request", request1.getID().toString(), new Gson().toJson(request1));
//        controller.create("request", request2.getID().toString(), new Gson().toJson(request2));
//        wait(1000);
//
//        JsonArray requests = controller.getAllFromIndex("request");
//        assertEquals(requests.size(), 3);
//        Rider returnedRider = new Gson().fromJson(controller.get("user", "id", rider.getID().toString()), Rider.class);
//        assertTrue(rider.equals(returnedRider));
    }
}
