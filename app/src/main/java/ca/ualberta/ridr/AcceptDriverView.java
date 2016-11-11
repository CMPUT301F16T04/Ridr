package ca.ualberta.ridr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

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

        //TODO get the driver using the info passed from the previous activity
        //will need to remove these, just here to test UI
        Vehicle vehicle = new Vehicle(1994, "chevy", "truck");
        final Driver driver = new Driver("Jeff", new Date(), "111", "email", "8675309", vehicle, "123");
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
                Rider rider = new Rider("joe", new Date(), "credit", "email", "phone");
                Request request = new Request("start", "end");

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
