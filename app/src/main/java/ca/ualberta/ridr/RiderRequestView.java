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
        setContentView(R.layout.activity_rider_request_view);

        startLocation = (EditText) findViewById(R.id.editStartLocationText);
        endLocation = (EditText) findViewById(R.id.editEndLocationText);

        Button addRequest = (Button) findViewById(R.id.createRequestButton);
        addRequest.setOnClickListener(new View.OnClickListener(){
            public void OnCLick(View v){

                //TODO pass a rider object to this view
                reqController.createRequest(Rider, startLocation.getText().toString(), endLocation.getText().toString());
            }
        });

    }
}
