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

public class DriverRidesViewTest extends ActivityInstrumentationTestCase2<DriverRidesView> {

    private Solo solo;
    /**
     * Instantiates a new Lonely twitter activity test.
     */
    public DriverRidesViewTest() {
        super(DriverRidesView.class);
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
//    A Driver should be able to see their rides
//    public void getDriverRides(){
//        DriverRidesView ridesView = (DriverRidesView) solo.getCurrentActivity();
//
//        // Set user
//        ridesView.setUser("kristy");
//        solo.assertCurrentActivity("Wrong activity", DriverRidesView.class);
//
//        // Check to see the user has more than one ride
//        RideController controller = driverRidesList.getrideController();
//        assert(controller.size() > 0);
//    }
//
//    @Override
//    public void tearDown() throws Exception{
//        solo.finishOpenedActivities();
//    }

}
