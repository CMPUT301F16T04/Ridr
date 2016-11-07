package ca.ualberta.ridr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class RiderRequestView extends AppCompatActivity {

    private EditText startLocation;
    private EditText endLocation;

    private UUID currentUUID;

    RequestController reqController = new RequestController();
    RiderController riderController = new RiderController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rider_main);

        //retrieve the current rider's UUID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            currentUUID = UUID.fromString(extras.getString("id"));
        }
        //from the UUID, get the rider object
        //TODO retrieve rider from elasticsearch using the rider controller


        startLocation = (EditText) findViewById(R.id.editStartLocationText);
        endLocation = (EditText) findViewById(R.id.editEndLocationText);

        Button addRequest = (Button) findViewById(R.id.createRequestButton);
        addRequest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Rider rider = null; // for now just so that we wont get compile errors
                reqController.createRequest(rider, startLocation.getText().toString(), endLocation.getText().toString());
            }
        });

    }
}
