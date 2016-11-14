package ca.ualberta.ridr;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RiderMainView extends Activity {
    //must extend activity, not appcompatactivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_main);
    }
}
