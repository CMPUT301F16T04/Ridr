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
//            String requestId = extras.getString(");
//        }
//        //TODO get the driver using the info passed from the previous activity


        String driverId = "475a3caa-88b5-46b2-9a44-cd02ef8a2d28";
        DriverController DC = new DriverController();
        final Driver driver = DC.getDriverFromServer(driverId);


        String riderId = "6a5f339c-2679-4e18-825f-2d6fc6cdc3e2";
        RiderController rideCon = new RiderController();
        final Rider rider = rideCon.getRiderFromServer(riderId);


        String requestId = "4d08b0e5-9bf7-45fb-b5ea-37a5cb03eeba";
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
                RideC.createRide(driver, request, rider);

                //we remove the request
                //TODO check if we need to do elastic things here too
                //requestCon.removeRequest(request, rider);
                //cant do while rider's request list is null

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
