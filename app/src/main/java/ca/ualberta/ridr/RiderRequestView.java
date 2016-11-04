package ca.ualberta.ridr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RiderRequestView extends AppCompatActivity {

    private EditText startLocation;
    private EditText endLocation;

    RequestController reqController = new RequestController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rider_request);

        startLocation = (EditText) findViewById(R.id.editStartLocationText);
        endLocation = (EditText) findViewById(R.id.editEndLocationText);

        Button addRequest = (Button) findViewById(R.id.createRequestButton);
        addRequest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //TODO pass a rider object to this view
                Rider rider = null; // for now just so that we wont get compile errors
                reqController.createRequest(rider, startLocation.getText().toString(), endLocation.getText().toString());
            }
        });

    }
}
