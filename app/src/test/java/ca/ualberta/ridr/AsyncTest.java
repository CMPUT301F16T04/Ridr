package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


/**
 * Created by Justin on 2016-11-11.
 */
    public class AsyncTest {
    @Test
    public void addRequestTest() {
        // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));
        //Test data creation
        verify(mockedController).create("user", rider.getID().toString(), new Gson().toJson(rider));
        verify(mockedController).create("request", request.getID().toString(), new Gson().toJson(request));
    }

    @Test
    public void getRequestTest(){
       // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));

        //Return request
        mockedController.get("request", "id", request.getID().toString());

        //Check data
        verify(mockedController).get("request", "id", request.getID().toString());
    }

    @Test
    public void addRequestTest() {
        // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));
        //Test data creation
        verify(mockedController).create("user", rider.getID().toString(), new Gson().toJson(rider));
        verify(mockedController).create("request", request.getID().toString(), new Gson().toJson(request));
    }

    @Test
    public void getRequestTest(){
        // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));

        //Return request
        mockedController.get("request", "id", request.getID().toString());

        //Check data
        verify(mockedController).get("request", "id", request.getID().toString());
    }
}
