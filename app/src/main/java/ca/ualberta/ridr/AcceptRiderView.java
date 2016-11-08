package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AcceptRiderView extends AppCompatActivity {

    private TextView requestInfo;
    private TextView requestFrom;
    private CharSequence isFrom;
    private Button acceptRider;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_rider);

        requestInfo = (TextView) findViewById(R.id.possible_request_info);
        requestFrom = (TextView) findViewById(R.id.request_from);
        acceptRider = (Button) findViewById(R.id.accept_rider);

        final Request request = null;
        //running from isolated activity wont work without real data though and lets not spent too much time mocking here

        isFrom = "Request From "+ request.getRider();

        requestFrom.setText(isFrom);

        //TODO put the request info into the other textview and somehow see fi we can make the name of rider clickable

        requestFrom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //TODO also pass the rider to the next activity so that we can display their info
                Intent intent = new Intent(activity, ProfileView.class);
                startActivity(intent);
            }
        });

        acceptRider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                DriverController DC = new DriverController();
                RequestController RC = new RequestController();
                //yet another fudge for now
                Driver driver = null;

                //three things need to happen if driver accepts the request
                //the drivers list of accepted requests is update
                // the requests bool is updated
                // the requests list of possible drivers is updatee
                DC.acceptRequest(driver, request);
                RC.accept(request);
                RC.addDriver(request, driver);
            }

        });
    }
}
