package ca.ualberta.ridr;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Storm on 2016-11-22.
 */

public class RequestsFromRidersViewTest extends ActivityInstrumentationTestCase2<DriverRequestsView> {

private Solo solo;

    /**
     * Instantiates a new DriverRequestsView activity test.
     */
    public RequestsFromRidersViewTest() {
        super(DriverRequestsView.class);
    }

    public void testEmptyRequestList(){
        Intent editInfoIntent = new Intent();
        editInfoIntent.putExtra("UUID", "test");
        setActivityIntent(editInfoIntent);
        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity("wrong activity", DriverRequestsView.class);
        assertTrue(solo.waitForText("You haven't accepted any requests yet!"));
        // Assertion of toast message, may not work

    }


    /**
     * TEST FOR US 2.01.01, DISPLAYING STATUS OF REQUESTS
     */
    public void testStatusOfRequests(){
        ArrayList<Request> requests = new ArrayList<Request>();
        Request testRequest = new Request("Bob", "pickup", "dropoff", new LatLng(123, 123), new LatLng(123, 123.5), new Date());
        testRequest.setAccepted(true); //so we can definitely see the "Status:" text
        requests.add(testRequest);
        //set the adapter, so the list is populated
        RequestAdapter customAdapter = new RequestAdapter(getActivity(), requests);
        ListView requestList = (ListView)getActivity().findViewById(R.id.requests_from_riders_list);
        requestList.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        solo.waitForText("Status:");


        //wanted to check to see if we could see the "Status:" text. Not sure if this will work
    }


    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
