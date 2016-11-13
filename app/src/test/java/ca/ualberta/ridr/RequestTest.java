package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Justin on 2016-11-03.
 */

public class RequestTest {
    @Test
    public void testRequestEquals(){
<<<<<<< HEAD
        Rider rider = null;
        Request request = new Request(rider,"Edmonton", "Timbuktu", "some day", "later");
=======
        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
        Request request = new Request(rider, "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
>>>>>>> origin/geoRequest
        assertTrue(request.equals(request));
    }
}
