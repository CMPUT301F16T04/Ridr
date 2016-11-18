package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class AcceptRiderView extends AppCompatActivity {

    private TextView requestInfo;
    private TextView requestFrom;
    private Button acceptRider;
    private UUID driverID;
    private UUID requestID;
    private Rider requestRider;
    private Request request;


    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_rider);

        requestInfo = (TextView) findViewById(R.id.possible_request_info);
        requestFrom = (TextView) findViewById(R.id.request_from);
        acceptRider = (Button) findViewById(R.id.accept_rider);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        {
            driverID = UUID.fromString(extras.getString("UUID"));
            requestID = UUID.fromString(extras.getString("RequestUUID"));
        } else {
            Log.i("Intent Extras Error", "Error getting driver and request ID from extras in AcceptRiderView");
            finish();
        }
        RequestController requestController = new RequestController();
        request = requestController.getRequestFromServer(requestID.toString());

        //got request, now need to get the rider of the request, since we aren't storing names in the request
        RiderController riderController = new RiderController();
        requestRider = riderController.getRiderFromServer(request.getRider());

        CharSequence isFrom = "Request From " + requestRider.getName();

        requestFrom.setText(isFrom);

        //make the requestFrom text view clickable
        requestFrom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //TODO also pass the rider to the next activity so that we can display their info
                Intent intent = new Intent(activity, ProfileView.class);
                intent.putExtra("RiderUUID", requestRider.getID().toString());
                startActivity(intent);
            }
        });

        //when clicking on the accept rider button, we check if we already accepted. If not,
        //add ourselves to list of prospectiveDrivers in the request (? maybe, still haven't got clearance from group)
        acceptRider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DriverController DC = new DriverController();
                RequestController RC = new RequestController();
                //yet another fudge for now

                //three things need to happen if driver accepts the request
                //the drivers list of accepted requests is update
                // the requests bool is updated
                // the requests list of possible drivers is updatee
                //DC.acceptRequest(driver, request);
                //RC.accept(request);
                //RC.addDriver(request, driver);
            }

        });
    }
}
