package ca.ualberta.ridr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RiderMainView extends Activity {

    private EditText startLocation;
    private EditText endLocation;
    private EditText fareInput;

    private TextView dateTextView;
    private TextView timeTextView;

    private Button addRequest;
    private Button dateButton;
    private Button timeButton;
    //private Toolbar toolbar;

    private String pickUpDateString;
    private String pickUpTimeString;

    private Calendar cal;
    private DatePickerDialog datePicker;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private UUID currentUUID; // UUID of the currently logged-in rider

    RequestController reqController = new RequestController();
    //RiderController riderController = new RiderController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rider_main);

        //toolbar = (Toolbar) findViewById(R.id.makeRequestToolbar);
        //setSupportActionBar(toolbar);

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
        fareInput = (EditText) findViewById(R.id.editFare);

        dateTextView = (TextView) findViewById(R.id.dateText);
        timeTextView = (TextView) findViewById(R.id.timeText);

        addRequest = (Button) findViewById(R.id.createRequestButton);
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //makes a date fragment when clicked
                DialogFragment frag = new DateSelector();
                frag.show(getFragmentManager(), "DatePicker");
            }
        });



        addRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Rider rider = null; // for now just so that we wont get compile errors
                reqController.createRequest(rider, startLocation.getText().toString(), endLocation.getText().toString() , "date", "time");
                Toast.makeText(RiderMainView.this, "request made", Toast.LENGTH_SHORT).show();

                // reset fields
                resetText();
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


    private void resetText(){
        //reset text inputs in the view
        startLocation.setText("Enter Start Location");
        endLocation.setText("Enter Destination");
    }

//    private void setDateField(){
//        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth){
//                cal.set(year, monthOfYear, dayofMonth);
//                dateTextView.setText(dateFormat.format(cal.getTime()));
//            }
//        })
//    }
}
