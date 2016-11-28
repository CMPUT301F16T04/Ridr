package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;


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

    private String username;
    private String driverName;
    private String requestId;

    private DriverController driverCon = new DriverController();
    private RideController rideCon = new RideController();
    private RequestController reqCon = new RequestController();
    private RiderController riderCon = new RiderController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_driver);

        driverEmail = (Button) findViewById(R.id.driver_email);
        driverPhone = (Button) findViewById(R.id.driver_phone);
        xProfile = (TextView) findViewById(R.id.x_profile);
        accept = (Button) findViewById(R.id.accept_button);

        //this is to fetch the data we need from the previous activity
        Intent intent = getIntent();
        ArrayList<String> ids = intent.getStringArrayListExtra("ids");
        if (ids != null) {
            driverName = ids.get(0);
            username = ids.get(1);
            requestId = ids.get(2);
        }
        else{
            //this is if there was some error retrieving data passed, just go back to prev activity
            finish();
        }


        final Driver driver = getDriver(driverName);
        final Request request  = getRequest(requestId);

        final String driverEmailStr = driver.getEmail();
        String driverPhoneStr = driver.getPhoneNumber();

        driverEmail.setText(driverEmailStr);
        driverPhone.setText(driverPhoneStr);
        xProfile.setText(capitalizeName(driverName));

        //if the user clicks the accept button state of the request is modified, a ride is created
        //and stored on server, and then we return to prev activity
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rideCon.createRide(driverName, request, username);
                reqCon.accept(request);

                //save pendingNotification for driver, upload to elastic search
                driver.setPendingNotification("You have been chosen as a Driver for a Ride! View Rides " +
                        "for more info.");
                try {
                    AsyncController asyncController = new AsyncController();
                    asyncController.create("user", driver.getID().toString(), new Gson().toJson(driver));
                    //successful account updating
                } catch (Exception e){
                    Log.i("Communication Error", "Could not communicate with the elastic search server");
                    return;
                }

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

        //if the user clicks the drivers displayed email we will want to take them to an email app
        driverEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application?rq=1
                // Nov 24 2016
                // author Jeremy Logan
                //note in order for this to work you must set up an email on your device
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{driverEmailStr});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AcceptDriverView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * gets the driver for the data we display on this view
     *
     * @param driverName used to fetch the driver
     * @return Driver object
     */
    public Driver getDriver(String driverName){
        Driver driver = driverCon.getDriverFromServerUsingName(driverName);
        return(driver);
    }


    /**
     * gets the request that we will need to create the ride
     *
     * @param requestId used to fetch the request
     * @return Request object
     */
    public Request getRequest(String requestId){
        Request request = reqCon.getRequestFromServer(requestId);

        //if we could not fetch the request and return null then... go back to previous activity?
        if(request==null) {
            finish();
        }

        return(request);
    }

    /** just some formatting, might not be necessary if the names are enforced
     *  to be capitalized but wont hurt to have this til then
     *
     * @param name the possibly lowercased name
     * @return the name with the first letter capitalized
     */
    private String capitalizeName(String name){
        return (name.substring(0,1).toUpperCase().concat(name.substring(1)));
    }


}
