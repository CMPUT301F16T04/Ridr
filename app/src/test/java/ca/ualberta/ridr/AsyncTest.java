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
<<<<<<< HEAD
    public class AsyncTest {
   /* @Test
=======
public class AsyncTest {
    @Test
>>>>>>> 2385fd7675cceed014c43f673d174e9e36f67d58
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
<<<<<<< HEAD
       // Create data
=======
        // Create data
>>>>>>> 2385fd7675cceed014c43f673d174e9e36f67d58
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));
<<<<<<< HEAD

        //Return request
        mockedController.get("request", "id", request.getID().toString());

        //Check data
        verify(mockedController).get("request", "id", request.getID().toString());
=======

        //Return request
        mockedController.get("request", "id", request.getID().toString());

        //Check data
        verify(mockedController).get("request", "id", request.getID().toString());
    }

    @Test
    public void getRequestTestFiltered(){
        // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);
        //Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));

        //Return request
        mockedController.getAllFromIndexFiltered("request", "rider", "Justin Barclay");

        //Check data
        verify(mockedController).getAllFromIndexFiltered("request", "rider", "Justin Barclay");
    }

    @Test
    public void getAllRequests(){
        // Create data
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request( "Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        Request request2 =  new Request( "Justin Barclay", "WEM", "Calgary", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        // /Pass data to controler
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("request", request.getID().toString(), new Gson().toJson(request));
        mockedController.create("request", request2.getID().toString(), new Gson().toJson(request2));

        // /Return request
        mockedController.getAllFromIndex("request");

        //Check data
        verify(mockedController).getAllFromIndex("request");
    }
    @Test
    public void addRideTest() {
        // Create data
        Ride ride = new Ride("Leeroy Jenkins", "Luke Skywalker", "Rebel Base, Hoth", "Degobah",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));
        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controler
        mockedController.create("ride", ride.getId().toString(), new Gson().toJson(ride));

        //Test data creation
        verify(mockedController).create("ride", ride.getId().toString(), new Gson().toJson(ride));
    }

    @Test
    public void getRideTest(){
        // Create data
        Ride ride = new Ride("Leeroy Jenkins", "Luke Skywalker", "Rebel Base, Hoth", "Degobah",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("ride", ride.getId().toString(), new Gson().toJson(ride));

        //Return request
        mockedController.get("ride", "id", ride.getId().toString());

        //Check data
        verify(mockedController).get("request", "id", ride.getId().toString());
    }

    @Test
    public void getRideFiltered(){
        // Create data
        Ride ride = new Ride("Leeroy Jenkins", "Luke Skywalker", "Rebel Base, Hoth", "Degobah",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));
        Ride ride2 = new Ride("Gandalf", "Frodo Baggins", "Bag End, The Shire", "Mount Doom, Mordor",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("ride", ride.getId().toString(), new Gson().toJson(ride));
        mockedController.create("ride", ride2.getId().toString(), new Gson().toJson(ride2));

        //Return request
        mockedController.getAllFromIndexFiltered("ride", "id", ride.getId().toString());

        //Check data
        verify(mockedController).getAllFromIndexFiltered("ride", "id", ride.getId().toString());
    }

    @Test
    public void getAllRides(){
        // Create data
        Ride ride = new Ride("Leeroy Jenkins", "Luke Skywalker", "Rebel Base, Hoth", "Degobah",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));
        Ride ride2 = new Ride("Gandalf", "Frodo Baggins", "Bag End, The Shire", "Mount Doom, Mordor",
                new Date(), new LatLng(100.54, -113.525),new LatLng(53.484775, -113.505067));

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("ride", ride.getId().toString(), new Gson().toJson(ride));
        mockedController.create("ride", ride2.getId().toString(), new Gson().toJson(ride2));

        //Return request
        mockedController.getAllFromIndex("ride");

        //Check data
        verify(mockedController).getAllFromIndex("ride");
    }

    @Test
    public  void addDriverTest(){
        Driver driver = new Driver("John Wick", new Date(),"5555 5555 5555 5555",
                "babyaga@gmail.com", "180-666-666-6666", "123456789");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("user", driver.getName(), new Gson().toJson(driver));

        //Check data
        verify(mockedController).create("user", driver.getID().toString(), new Gson().toJson(driver));

    }

    @Test
    public void getDriverTest(){
        Driver driver = new Driver("John Wick", new Date(),"5555 5555 5555 5555",
                "babyaga@gmail.com", "180-666-666-6666", "123456789");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("user", driver.getName(), new Gson().toJson(driver));
        mockedController.get("user", driver.getName(), driver.getName());

        //Check data
        verify(mockedController).get("user", driver.getName(), driver.getName());
    }

    @Test
    public void getDriverTestFiltered(){
        Driver driver = new Driver("John Wick", new Date(),"5555 5555 5555 5555",
                "babyaga@gmail.com", "180-666-666-6666", "123456789");

        Driver driver1 = new Driver("John Matrix", new Date(), "5555 5555 55555 55555",
                "commando@infinitebullets.com", "180-666-666-6666", "123456789");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.getAllFromIndexFiltered("user", driver.getName(), driver.getName());

        //Test data
        verify(mockedController).getAllFromIndexFiltered("user", driver.getName(), driver.getName());


    }

    @Test
    public void getAllDrivers(){
        Driver driver = new Driver("John Wick", new Date(),"5555 5555 5555 5555",
                "babyaga@gmail.com", "180-666-666-6666", "123456789");

        Driver driver1 = new Driver("John Matrix", new Date(), "5555 5555 55555 55555",
                "commando@infinitebullets.com", "180-666-666-6666", "123456789");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.getAllFromIndex("user");

        //Test data
        verify(mockedController).getAllFromIndex("user");
    }


    @Test
    public  void addRiderTest() {
        // Create data
        Rider rider = new Rider("Ben Kenobi", new Date(), "5555 5555 5555 5555",
                "usetheforce@ualberta.ca", "780-995-3417");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("request", rider.getID().toString(), new Gson().toJson(rider));

        //Test data creation
        verify(mockedController).create("request", rider.getID().toString(), new Gson().toJson(rider));
    }

    @Test
    public void getRiderTest(){
        // Create data
        Rider rider = new Rider("Ben Kenobi", new Date(), "5555 5555 5555 5555",
                "usetheforce@ualberta.ca", "780-995-3417");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.get("user","name", rider.getName());

        //Test data creation
        verify(mockedController).get("user","name", rider.getName());
    }

    @Test
    public void getRiderTestFiltered(){
        // Create data
        Rider rider = new Rider("Ben Kenobi", new Date(), "5555 5555 5555 5555",
                "usetheforce@ualberta.ca", "780-995-3417");
        Rider rider2 = new Rider("FN-2187", new Date(), "5555 5555 5555 5555",
                "traitor@ualberta.ca", "780-995-3417");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("user", rider2.getID().toString(), new Gson().toJson(rider2));
        //Perform test op
        mockedController.getAllFromIndexFiltered("user","name", rider.getName());

        //Test data creation
        verify(mockedController).getAllFromIndexFiltered("user","name", rider.getName());
    }

    @Test
    public void getAllRiders(){
        // Create data
        Rider rider = new Rider("Ben Kenobi", new Date(), "5555 5555 5555 5555",
                "usetheforce@ualberta.ca", "780-995-3417");
        Rider rider2 = new Rider("FN-2187", new Date(), "5555 5555 5555 5555",
                "traitor@ualberta.ca", "780-995-3417");

        //Create mock controller
        AsyncController mockedController = mock(AsyncController.class);

        //Pass data to controller
        mockedController.create("user", rider.getID().toString(), new Gson().toJson(rider));
        mockedController.create("user", rider2.getID().toString(), new Gson().toJson(rider2));
        //Perform test op
        mockedController.getAllFromIndex("user");

        //Test data creation
        verify(mockedController).getAllFromIndex("user");
>>>>>>> 2385fd7675cceed014c43f673d174e9e36f67d58
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
    }*/
}
