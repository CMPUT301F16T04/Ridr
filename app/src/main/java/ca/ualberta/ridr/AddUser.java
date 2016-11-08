package ca.ualberta.ridr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddUser extends Activity {
    EditText usernameEditText;
    EditText dobEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText creditEditText;
    Button createAccountButton;
    Boolean firstClickUsername = false;
    Boolean firstClickDOB = false;
    Boolean firstClickEmail = false;
    Boolean firstClickPhone = false;
    Boolean firstClickCredit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        //defining view objects
        usernameEditText = (EditText) findViewById(R.id.username_add_account_edit_text);
        dobEditText = (EditText) findViewById(R.id.dob_add_account_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_add_account_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_add_account_edit_text);
        creditEditText = (EditText) findViewById(R.id.credit_add_account_edit_text);
        createAccountButton = (Button) findViewById(R.id.create_account_button);

        //first click logic
        usernameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!firstClickUsername) {
                    usernameEditText.setTextColor(Color.BLACK);
                    usernameEditText.setText("");
                    firstClickUsername = true;
                }
                return false;
            }
        });
        dobEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!firstClickDOB) {
                    dobEditText.setTextColor(Color.BLACK);
                    dobEditText.setText("");
                    firstClickDOB = true;
                }
                return false;
            }
        });
        emailEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!firstClickEmail) {
                    emailEditText.setTextColor(Color.BLACK);
                    emailEditText.setText("");
                    firstClickEmail = true;
                }
                return false;
            }
        });
        phoneEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!firstClickPhone) {
                    phoneEditText.setTextColor(Color.BLACK);
                    phoneEditText.setText("");
                    firstClickPhone = true;
                }
                return false;
            }
        });
        creditEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!firstClickCredit) {
                    creditEditText.setTextColor(Color.BLACK);
                    creditEditText.setText("");
                    firstClickCredit = true;
                }
                return false;
            }
        });






    }
}
