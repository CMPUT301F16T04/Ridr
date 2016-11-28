package ca.ualberta.ridr;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.SingleLaunchActivityTestCase;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by kristynewbury on 2016-11-27.
 *
 * these tests do not run... says empty test suite, cannot figure out why
 *
 */

@RunWith(AndroidJUnit4.class)
public class ProfileViewTest extends ActivityInstrumentationTestCase2<ProfileView>{

    private Solo solo;
    private String name = "Joe";
    private Activity activity;


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
        intent.putExtra("username", "joe");
        setActivityIntent(intent);
        activity = getActivity();
    }
    @Override
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void runTest(){
        ProfileView profileView = (ProfileView) solo.getCurrentActivity();
        solo.assertCurrentActivity("Wrong activity", ProfileView.class);

        TextView textView = (TextView) solo.getView(R.id.x_name);

        // Validate the text on the TextView
        assertEquals("Text should be the field value", name ,
                textView.getText().toString());

    }
    @Test
    public void testExtras() {
        String str = "";
        str = activity.getIntent().getStringExtra("username");
        assertEquals(name, str);
    }

    @Test
    public void testProfileView(){


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
