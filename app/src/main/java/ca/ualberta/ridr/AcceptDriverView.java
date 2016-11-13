package ca.ualberta.ridr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.UUID;


public class AcceptDriverView extends Activity {

    private TextView driverEmail;
    private TextView driverPhone;
    private Button accept;
    private TextView xProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_driver);

        driverEmail = (TextView) findViewById(R.id.driver_email);
        driverPhone = (TextView) findViewById(R.id.driver_phone);
        xProfile = (TextView) findViewById(R.id.x_profile);
        accept = (Button) findViewById(R.id.accept_button);

//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            String riderId= extras.getString("riderId");
//            String driverId= extras.getString("driverId");
//        }
//        //TODO get the driver using the info passed from the previous activity


        String driverId = "475a3caa-88b5-46b2-9a44-cd02ef8a2d28";
        DriverController DC = new DriverController();
        final Driver driver = DC.getDriverFromServer(driverId);


        String riderId = "6a5f339c-2679-4e18-825f-2d6fc6cdc3e2";
        RiderController rideCon = new RiderController();
        final Rider rider = rideCon.getRiderFromServer(riderId);

        AsyncController con = new AsyncController();
        JsonObject s = con.create("user",riderId, new Gson().toJson(rider));
        System.out.println(s);

        LatLng coords = new LatLng(0,0);
        final Request request = new Request(rider, "start", "end", coords, coords, new Date());


        String driverEmailStr = driver.getEmail();
        String driverPhoneStr = driver.getPhoneNumber();

        String profileString = "'s Profile";

        driverEmail.setText(driverEmailStr);
        driverPhone.setText(driverPhoneStr);
        xProfile.setText(driver.getName() + profileString);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO also need the id of the rider currently logged in so we can create a ride and add it to their rides at this point
                //also need to know what request we are dealing with so that we can create the ride...

                //will need to remove these eventually too


                //need to remove these eventually when we retrieve real data
                RequestController RC = new RequestController();
                RideController RideC = new RideController();

                RideC.createRide(driver, request, rider);

                //do we remove the request now from the riders list? probably?
                RC.removeRequest(request, rider);

            }
        });
        driverPhone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // pretty well found at link: http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                // accessed on Nov. 10 2016
                // answered by AAnkit
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+driver.getPhoneNumber()));
                startActivity(callIntent);
            }
        });
        driverEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(AcceptDriverView.this, "going to send an email!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
