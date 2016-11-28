package ca.ualberta.ridr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import org.junit.Test;

import java.util.Date;

/**
 * Created by Justin on 2016-11-12.
 */

    public class GeoViewRequestTest extends ActivityInstrumentationTestCase2<GeoView> {

    private Solo solo;
    /**
     * Instantiates a new Lonely twitter activity test.
     */
    public GeoViewRequestTest() {
        super(GeoView.class);
    }

    /**
     * Test start.
     *
     * @throws Exception the exception
     */
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void initializeMap(){
        GeoView maps = (GeoView) solo.getCurrentActivity();
        maps.setTest(true);
        solo.assertCurrentActivity("Wrong activity", GeoView.class);
        assertEquals(maps.countMarkers(), 0);
    }

//    public void filterRequests(){
//        GeoView maps = (GeoView) solo.getCurrentActivity();
//        maps.setTest(true);
//        solo.assertCurrentActivity("Wrong activity", GeoView.class);
//
//        //Simulate filtering one request
//        Rider rider = new Rider("Justin Barclay", new Date(), "5555 5555 5555 5555", "jbarclay@ualberta.ca", "780-995-3417");
//        Request request = new Request(rider, "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
//        maps.getRequestController().add(request);
//        // Make sure callback forces the map to update it's state
//        maps.callback();
//
//        assertEquals(maps.countMarkers(), 1);
//    }
    

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
