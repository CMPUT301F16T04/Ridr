package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class RiderMainView extends Activity {

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
        if (extras != null) {
            currentUUID = UUID.fromString(extras.getString("id"));
        }
        //from the UUID, get the rider object
        //TODO retrieve rider from elasticsearch using the rider controller


        startLocation = (EditText) findViewById(R.id.editStartLocationText);
        endLocation = (EditText) findViewById(R.id.editEndLocationText);

        Button addRequest = (Button) findViewById(R.id.createRequestButton);
        addRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Rider rider = null; // for now just so that we wont get compile errors
                reqController.createRequest(rider, startLocation.getText().toString(), endLocation.getText().toString());
                Toast.makeText(RiderMainView.this, "request made", Toast.LENGTH_SHORT).show();

                // reset fields
                startLocation.setText("Enter Start Location");
                endLocation.setText("Enter Destination");
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rider_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.mainRiderMenuEditUserInfo:
                Toast.makeText(RiderMainView.this, "Edit User Info", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mainRiderMenuViewRequests:
                Toast.makeText(RiderMainView.this, "View Requests", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
