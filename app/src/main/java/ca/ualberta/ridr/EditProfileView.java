package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class EditProfileView extends Activity {


    private UUID currentUUID; // UUID of the currently logged-in rider
    private String riderName; // string of the curretn UUID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            riderName = extras.getString("Name");
        }
    }

    /**
     * finds views by their ID's and assigns them to their respective variable
     */
    private void setViews(){

    }


}
