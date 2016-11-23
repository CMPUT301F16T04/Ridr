package ca.ualberta.ridr;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Test;

/**
 * Created by Storm on 2016-11-22.
 */

public class RequestsFromRidersViewTest extends ActivityInstrumentationTestCase2<RequestsFromRidersView> {

private Solo solo;

    /**
     * Instantiates a new RequestsFromRidersView activity test.
     */
    public RequestsFromRidersViewTest() {
        super(RequestsFromRidersView.class);
    }

    @Test
    public void testEmptyRequestList(){
        Intent editInfoIntent = new Intent();
        editInfoIntent.putExtra("UUID", "test");
        setActivityIntent(editInfoIntent);
        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity("wrong activity", RequestsFromRidersView.class);
        assertTrue(solo.waitForText("You haven't accepted any requests yet!"));
        // Assertion of toast message, may not work

    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
