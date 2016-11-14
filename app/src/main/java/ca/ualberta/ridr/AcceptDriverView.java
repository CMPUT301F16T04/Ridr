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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/**
 *  This view displays a profile of a possible driver for a request,
 *  the user can click the contact info of the driver and will be taken to
 *  appropriate apps to deal with contacting the driver,
 *  the user can also accept the driver and create a ride.
 *
 */



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


        Intent intent = getIntent();
        ArrayList<String> ids = intent.getStringArrayListExtra("ids");
        //if (ids != null) {
            final String riderId= ids.get(0);
            final String driverId= ids.get(1);
            final String requestId = ids.get(2);
        //}
//        //TODO get the driver using the info passed from the previous activity


        DriverController DC = new DriverController();
        final Driver driver = DC.getDriverFromServer(driverId);

        //RiderController rideCon = new RiderController();
        //final Rider rider = rideCon.getRiderFromServer(riderId);

        final RequestController requestCon = new RequestController();
        final Request request = requestCon.getRequestFromServer(requestId);
        //unhandled null return possibility, not quite sure at the moment what to do if null



        String driverEmailStr = driver.getEmail();
        String driverPhoneStr = driver.getPhoneNumber();

        String profileString = "'s Profile";

        driverEmail.setText(driverEmailStr);
        driverPhone.setText(driverPhoneStr);
        xProfile.setText(driver.getName() + profileString);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RideController RideC = new RideController();
                RideC.createRide(driverId, request, riderId);

                //we remove the request
                //TODO check if we need to do elastic things here too

                //need to set request attr isAccepted to true

                //requestCon.removeRequest(request, rider);
                //cant do while rider's request list is null

                finish();

            }
        });
        driverPhone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // found at link: http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
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
                //TODO lookup a way to do this, previously found one but not sure if there are apps that can be transferred to?
                Toast.makeText(AcceptDriverView.this, "going to send an email later!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
