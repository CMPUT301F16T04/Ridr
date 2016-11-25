package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
}
