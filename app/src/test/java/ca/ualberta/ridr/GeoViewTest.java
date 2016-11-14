package ca.ualberta.ridr;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.Test;

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
        Log.d("TAG1", "setUp()");
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void initializeMap(){
    }

    public void testClickTweetList(){

        LonelyTwitterActivity activity = (LonelyTwitterActivity) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong Activity", LonelyTwitterActivity.class);
        solo.clickOnButton("Clear");

        solo.enterText((EditText) solo.getView(R.id.body), "Test Tweet!");
        solo.clickOnButton("Save");
        solo.waitForText("Test Tweet!");


        final ListView oldTweetList = activity.getOldTweetsList();
        Tweet tweet = (Tweet) oldTweetList.getItemAtPosition(0);
        solo.clickInList(0);

        solo.assertCurrentActivity("Wrong activity", EditTweetActivity.class);

        solo.waitForText("Edit Text");
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", LonelyTwitterActivity.class);
    }


    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
