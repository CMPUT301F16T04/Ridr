package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * This is the view that is called when a user profile would like to be edited. It's populated with
 * info from the current user calling the activity.
 */
public class EditProfileView extends Activity {

    private String userName; // string of the current UUID

    private EditText editVehicleView;
    private EditText editEmailView;
    private EditText editPhoneView;

    private TextView vehicleTextView;

    private Button saveChangesButton;

    private String phoneStr;
    private String emailStr;
    private String vehicleStr;

    private DriverController driverController;
    private RiderController riderController;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        driverController = new DriverController();
        riderController = new RiderController();
        //retrieve intent from previous activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            userName = extras.getString("Name");

        }

        setViews();
        user = getUser(userName);
        getInfo(user, userName);
        //hide or show vehicle info depending on driver status
        hideShowVehicle(driverStatus(user));
        //set user's info to the EditText views
        setEditableTextInfo(driverStatus(user));

        saveChangesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getNewUserInfo(driverStatus(user));
                Toast.makeText(EditProfileView.this, "Saving changes", Toast.LENGTH_SHORT).show();
                if(driverStatus(user)){
                    driverController.saveChanges(userName, phoneStr, emailStr, vehicleStr);
                } else{
                    riderController.saveChanges(userName, phoneStr, emailStr);
                }
                finish();
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
     * retrieve's the user's info from the EditText views
     * @param driverStatus true is the user is a driver
     */
    private void getNewUserInfo(boolean driverStatus){
        phoneStr = editPhoneView.getText().toString();
        emailStr = editEmailView.getText().toString();
        if(driverStatus){
            vehicleStr = editVehicleView.getText().toString();
        }

    }

    /**
     * retieves the user from the database
     * @param username the username of the user
     * @return a User object
     */
    private User getUser(String username){
        return new Gson().fromJson(new AsyncController().get("user", "name", username), User.class);
    }


    /**
     * returns true if the user is a driver
     * @param user jsonobject user
     * @return the isDriver boolean
     */
    private boolean driverStatus(User user){
        return user.isDriver();
    }
    /**
     * retrieves phone number, email, and vehicle information from a user Json object
     * @param user a jsonobject user
     */
    private void getInfo(User user, String username){
        if(driverStatus(user)){
            Driver driver = driverController.getDriverFromServerUsingName(username);
            phoneStr = driver.getPhoneNumber();
            emailStr = driver.getEmail();
            vehicleStr = driver.getVehicleDescription();
        } else{
            Rider rider = riderController.getRiderFromServerUsingName(username);
            phoneStr = rider.getPhoneNumber();
            emailStr = rider.getEmail();
        }
    }


}
