package ca.ualberta.ridr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

public class EditProfileView extends AppCompatActivity {

    private EditText editDateOfBirth;
    private EditText editCreditCard;
    private EditText editEmail;
    private EditText editPhone;

    private UUID currentUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        //retrieve the current rider's UUID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            currentUUID = UUID.fromString(extras.getString("id"));
        }

        //from the UUID, get the rider object
        //TODO retrieve user (rider/driver) from elasticsearch using the rider and driver controllers

        editDateOfBirth = (EditText) findViewById(R.id.chageDOBText);
        editCreditCard = (EditText) findViewById(R.id.changeCreditText);
        editEmail = (EditText) findViewById(R.id.changeEmailText);
        editPhone = (EditText) findViewById(R.id.changeEmailText);

        Button confirmChange = (Button) findViewById(R.id.saveChangesButton);
        Button cancelChanges = (Button) findViewById(R.id.cancelProfileEditButton);

        confirmChange.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(EditProfileView.this, "changes saved", Toast.LENGTH_SHORT).show();
                // TODO call the rider/drive controller to change the user

                editDateOfBirth.setText("");
                editCreditCard.setText("");
                editEmail.setText("");
                editPhone.setText("");

                // TODO possibly return to the previous activity
            }
        });

        cancelChanges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(EditProfileView.this, "cancelling changes", Toast.LENGTH_SHORT).show();

                editDateOfBirth.setText("");
                editCreditCard.setText("");
                editEmail.setText("");
                editPhone.setText("");

                //TODO return to the previous activity
            }
        });
    }


}
