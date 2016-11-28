package ca.ualberta.ridr;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

/**
 * Created by jferris on 28/11/16.
 */

public class offlineTest {
    @Test
    public void offlineSingletonTest() {
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        Request request2 = new Request(rider.getID().toString(), "Edmonton", "Timbuktu", coords, coords, new Date());

        OfflineSingleton offlineSingleton = OfflineSingleton.getInstance();

        offlineSingleton.addDriverAcceptance(request);
        offlineSingleton.addRiderRequest(request2);
    }
}
