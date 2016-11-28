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

import com.google.gson.JsonObject;

/**
 * This view displays the info of the user when a username was presented and clicked on
 * one note is that the user in prev activity could have been either a rider or a driver
 * and each has specific info , so we will need to have their type passed to us.
 */

public class ProfileView extends Activity {

    private Button userEmail;
    private Button userPhone;
    private TextView xName;
    private TextView vehicleInfo;
    private TextView vehicleTitle;
    private Driver driver;
    private Rider rider;

    private DriverController driverCon = new DriverController();
    private RiderController riderCon = new RiderController();

    private String username;
    private String userEmailStr;
    private  String userPhoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        userEmail = (Button) findViewById(R.id.user_email);
        userPhone = (Button) findViewById(R.id.user_phone);
        xName = (TextView) findViewById(R.id.x_name);
        vehicleTitle = (TextView) findViewById(R.id.vehicle_title);

        JsonObject user = getUser(username);

        //user was a driver (possibly both)
        System.out.println("The bool!");
        System.out.println(user.get("isDriver").toString());
        Log.i("the user gotten", user.get("isDriver").toString());
        if(user.get("isDriver").toString().equals("true")){
            driver = getDriver(username);
            userEmailStr = driver.getEmail();
            userPhoneStr = driver.getPhoneNumber();
            vehicleInfo = (TextView) findViewById(R.id.vehicle_info);
            vehicleInfo.setText(driver.getVehicleDescription());
        }
        //else user was a rider
        else{
            rider = getRider(username);
            userEmailStr = rider.getEmail();
            userPhoneStr = rider.getPhoneNumber();
            vehicleTitle.setVisibility(View.GONE);

        }

        userEmail.setText(userEmailStr);
        userPhone.setText(userPhoneStr);
        xName.setText(capitalizeName(username));

        //if the user clicks the drivers displayed phone number we take them to a phone app and predial for them
        userPhone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // found at link: http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                // accessed on Nov. 10 2016
                // answered by AAnkit
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+userPhoneStr));
                startActivity(callIntent);
            }
        });

        //if the user clicks the drivers displayed email we will want to take them to an email app
        userEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application?rq=1
                // Nov 24 2016
                // author Jeremy Logan
                //note in order for this to work you must set up an email on your device
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{userEmailStr});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProfileView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * gets the user for the data we display on this view
     *
     * @param username used to fetch the driver
     * @return Driver object
     */
    public Driver getDriver(String username){
        return(driverCon.getDriverFromServerUsingName(username));
    }

    /**
     * gets the user for the data we display on this view
     *
     * @param username used to fetch the rider
     * @return Driver object
     */
    public Rider getRider(String username){
        return(riderCon.getRiderFromServerUsingName(username));
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

    /**
     * gets a user but we dont cast to rider or driver yet
     * because we have to a check first
     *
     * @param username the username of the user
     * @return a jsonobject user
     */
    private JsonObject getUser(String username){
        AsyncController controller = new AsyncController();
        JsonObject user = controller.get("user", "name", username);
        Log.i("the user gotten", user.get("isDriver").toString());
        return(user);
    }

}
