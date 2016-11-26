package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class EditProfileView extends Activity {


    private UUID currentUUID; // UUID of the currently logged-in rider
    private String riderName; // string of the current UUID

    private EditText editVehicleView;
    private EditText editEmailView;
    private EditText editPhoneView;

    private TextView vehicleTextView;

    private Button saveChangesButton;

    private String phoneStr;
    private String emailStr;
    private String vehicleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //TODO set user controller
        //retrieve intent from previous activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            riderName = extras.getString("Name");
        }

        setViews();
        //TODO retrieve user's info
        phoneStr = "780-555-1234";
        emailStr = "someone@email.com";
        vehicleStr = "car";
        //TODO hide or show vehicle info depending on driver status
        hideShowVehicle(false);
        //TODO set user's info to the EditText views

        setEditableTextInfo(false);
        //TODO make button
        saveChangesButton.setOnClickListener(new View.OnClickListener(){
            public void Onclick(View v){
                getNewUserInfo(false);
                //TODO update user's info through the controller
                Toast.makeText(EditProfileView.this, "saving changes", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

    }

    /**
     * finds views by their ID's and assigns them to their respective variable
     */
    private void setViews(){
        editVehicleView = (EditText) findViewById(R.id.editVehicleInfo);
        editEmailView = (EditText) findViewById(R.id.editEmail);
        editPhoneView = (EditText) findViewById(R.id.editPhone);

        vehicleTextView = (TextView) findViewById(R.id.textVehicleEdit);

        saveChangesButton = (Button) findViewById(R.id.finishEditButton);
    }

    /**
     * shows the TextView and EditText view if the user is a rider
     * @param driverStatus true if the user is a driver
     */
    private void hideShowVehicle(boolean driverStatus){
        if(driverStatus){
            editVehicleView.setVisibility(EditText.VISIBLE);
            vehicleTextView.setVisibility(TextView.VISIBLE);
        } else{
            editVehicleView.setVisibility(EditText.INVISIBLE);
            vehicleTextView.setVisibility(TextView.INVISIBLE);
        }
    }

    /**
     * Sets the test in the EditText views to the user's current information
     * @param driverStatus true if the user is a driver
     */
    private void setEditableTextInfo(boolean driverStatus){
        editPhoneView.setText(phoneStr);
        editEmailView.setText(emailStr);
        if(driverStatus){
            editVehicleView.setText(vehicleStr);
        }
    }

    /**
     * retrives the user's info from the EditText views
     * @param driverStatus true is the user is a driver
     */
    private void getNewUserInfo(boolean driverStatus){
        phoneStr = editPhoneView.getText().toString();
        emailStr = editEmailView.getText().toString();
        if(driverStatus){
            vehicleStr = editVehicleView.getText().toString();
        }

    }


}
