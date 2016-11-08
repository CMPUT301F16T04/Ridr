package ca.ualberta.ridr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddUserView extends Activity {
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

        //Create account Button code.
        createAccountButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //all edit text parsing logic

                //if the username edit text is empty or hasn't been changed
                String formattedNameString = usernameEditText.getText().toString().trim();
                if(!firstClickUsername){
                    usernameEditText.setError("You must supply a name.");
                    return;
                }
                if(TextUtils.isEmpty(formattedNameString)){
                    usernameEditText.setError("The Name Field cannot be empty.");
                    return;
                }

                //if the dob edit text is empty or hasn't been changed
                String formattedDateString = dobEditText.getText().toString().trim();
                if(!firstClickDOB){
                    dobEditText.setError("You must provide a date of birth.");
                    return;
                }
                if(TextUtils.isEmpty(formattedDateString)){
                    dobEditText.setError("The Date Field cannot be empty.");
                    return;
                }
                Date DOB = returnsValidDate(formattedDateString);
                if((DOB == null)){
                    dobEditText.setError("Your date is not a valid date. It must be in format YYYY/MM/DD.");
                    return;
                }

                //if the email edit text is empty or hasn't been changed
                String formattedEmailString = emailEditText.getText().toString().trim();
                if(!firstClickEmail){
                    emailEditText.setError("You must provide an email.");
                    return;
                }
                if(TextUtils.isEmpty(formattedEmailString)){
                    emailEditText.setError("The Email Field cannot be empty.");
                    return;
                }

                //if the email edit text is empty or hasn't been changed
                String formattedPhoneString = phoneEditText.getText().toString().trim();
                if(!firstClickPhone){
                    phoneEditText.setError("You must provide a phone number.");
                    return;
                }
                if(TextUtils.isEmpty(formattedPhoneString)){
                    phoneEditText.setError("The Phone Field cannot be empty.");
                    return;
                }

                //if the email edit text is empty or hasn't been changed
                String formattedCreditString = creditEditText.getText().toString().trim();
                if(!firstClickCredit){
                    creditEditText.setError("You must provide a credit card, for payment processing.");
                    return;
                }
                if(TextUtils.isEmpty(formattedCreditString)){
                    creditEditText.setError("The Credit Card Field cannot be empty.");
                    return;
                }

                //successful account creation
                Toast.makeText(AddUserView.this, "Making Account!", Toast.LENGTH_SHORT).show();
                //save account in elastic search
                Rider rider = new Rider(formattedNameString, DOB,
                        formattedCreditString, formattedEmailString, formattedPhoneString);
                Driver driver = new Driver(formattedNameString, DOB,
                        formattedCreditString, formattedEmailString, formattedPhoneString, null);
                RiderController.AddRiderTask addRiderTask = new RiderController.AddRiderTask();
                DriverController.AddDriverTask addDriverTask = new DriverController.AddDriverTask();
                addRiderTask.execute(rider);
                addDriverTask.execute(driver);
                
                finish();
            }
        });




    }


    private Date returnsValidDate(String formattedDateString){
        //got idea from http://www.java2s.com/Tutorial/Java/0120__Development/CheckifaStringisavaliddate.htm

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        Date inputDate;
        Date currentDate = new Date();

        dateFormat.setLenient(false);
        try {
            inputDate = dateFormat.parse(formattedDateString);
        } catch (ParseException pe) {
            //date was not in correct format
            return null;
        }
        if(inputDate.after(currentDate)){
            //date of birth is after current date, shouldn't happen
            return null;
        }
        return inputDate;
    }
}
