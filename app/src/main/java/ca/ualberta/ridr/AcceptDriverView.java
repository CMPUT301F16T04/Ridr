package ca.ualberta.ridr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class AcceptDriverView extends AppCompatActivity {

    private TextView driverInfo;
    private Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_driver);

        driverInfo = (TextView) findViewById(R.id.driver_info);
        accept = (Button) findViewById(R.id.accept_button);

        //TODO get the driver using the info passed from the previous activity
        final Driver driver = null;
        String driver_description = driver.toString();

        driverInfo.setText(driver_description);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO also need the id of the rider currently logged in so we can create a ride and add it to their rides at this point
                //also need to know what request we are dealing with so that we can create the ride...

                Rider rider = null;
                Request request = null;

                RequestController RC = new RequestController();
                RideController RideC = new RideController();

                Ride ride = new Ride(driver, rider, RC.getPickup(request), RC.getDropoff(request), new Date());

                //do we remove the request now from the riders list of requests to add it to list of rides?
                RC.removeRequest(request, rider);

                RideC.addRide(rider, driver, request);
            }
        });
    }
}
