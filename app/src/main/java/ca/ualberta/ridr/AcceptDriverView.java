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

    private String riderId;
    private String driverId;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_driver);

        driverEmail = (TextView) findViewById(R.id.driver_email);
        driverPhone = (TextView) findViewById(R.id.driver_phone);
        xProfile = (TextView) findViewById(R.id.x_profile);
        accept = (Button) findViewById(R.id.accept_button);

        //this is to fetch the data we need from the previous activity
        Intent intent = getIntent();
        ArrayList<String> ids = intent.getStringArrayListExtra("ids");
        if (ids != null) {
            riderId= ids.get(0);
            driverId= ids.get(1);
            requestId = ids.get(2);
        }
        else{
            //this is if there was some error retrieving data passed, just go back to prev activity
            finish();
        }

//just here for testing, might leave for now.
//        String riderId = "6a5f339c-2679-4e18-825f-2d6fc6cdc3e2";
//        String driverId = "475a3caa-88b5-46b2-9a44-cd02ef8a2d28";
//        String requestId = "4d08b0e5-9bf7-45fb-b5ea-37a5cb03eeba";


        final Driver driver = getDriver(driverId);
        final Request request  = getRequest(requestId);

        String driverEmailStr = driver.getEmail();
        String driverPhoneStr = driver.getPhoneNumber();

        String profileString = "'s Profile";

        driverEmail.setText(driverEmailStr);
        driverPhone.setText(driverPhoneStr);
        xProfile.setText(driver.getName() + profileString);

        //if the user clicks the accept button state of the request is modified, a ride is created
        //and stored on server, and then we return to prev activity
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RideController RideC = new RideController();
                RideC.createRide(driverId, request, riderId);

                RequestController reqCon = new RequestController();
                reqCon.setRequestAccepted(request);

                //TODO once we have a user request list we can uncomment this
                //requestCon.removeRequest(request, rider);
                //cant do while rider's request list is null

                finish();

            }
        });

        //if the user clicks the drivers displayed phone number we take them to a phone app and predial for them
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

        //if the user clicks the drivers displaye email we will want to take them to an email app
        driverEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO lookup a way to do this, previously found one but not sure if there are apps that can be transferred to?
                Toast.makeText(AcceptDriverView.this, "going to send an email later!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * gets the driver for the data we display on this view
     *
     * @param driverId
     * @return Driver
     */
    public Driver getDriver(String driverId){
        DriverController DC = new DriverController();
        Driver driver = DC.getDriverFromServer(driverId);
        return(driver);
    }


    /**
     * gets the request that we will need to create the ride
     *
     * @param requestId
     * @return Request
     */
    public Request getRequest(String requestId){
        RequestController requestCon = new RequestController();
        Request request = requestCon.getRequestFromServer(requestId);

        //if we could not fetch the request and return null then... go back to previous activity?
        if(request.equals(null)) {
            finish();
        }

        return(request);
    }

}
