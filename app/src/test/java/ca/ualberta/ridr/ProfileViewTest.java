package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.Test;

/**
 * Created by kristynewbury on 2016-11-27.
 */

public class ProfileViewTest extends ActivityInstrumentationTestCase2<ProfileView> {

    private Solo solo;
    /**
     * Instantiates a new Lonely twitter activity test.
     */
    public ProfileViewTest() {
        super(ProfileView.class);
    }

    /**
     * Test start.
     *
     * @throws Exception the exception
     */
    public void testStart() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("", "joe");
        setActivityIntent(intent);
        Activity activity = getActivity();
    }
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Test
    public void testinitializeForDriver(){

        String name = "Joe";

        ProfileView profileView = (ProfileView) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong activity", ProfileView.class);

        TextView textView = (TextView) solo.getView(R.id.x_name);

        // Validate the text on the TextView
        assertEquals("Text should be the field value", name ,
                textView.getText().toString());

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
