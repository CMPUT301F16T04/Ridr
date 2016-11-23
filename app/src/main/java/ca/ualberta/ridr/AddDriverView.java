package ca.ualberta.ridr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.DatePickerDialog.*;

/**
 * Add Driver view
 * Created by Justin Barclay Nov 22, 2016
 *
 * This activity displays the add user screen, and handles the text input logic, and parsing of data.
 * Also checks to see if the user we are attempting to make is already in the database, and adds the
 * user if the user is valid.
 */
public class AddDriverView extends Activity {

    EditText vehicleEditText;
    /**
     * The Username edit text.
     */
    EditText usernameEditText;
    /**
     * The Dob edit text.
     */
    EditText dobEditText;
    /**
     * The Email edit text.
     */
    EditText emailEditText;
    /**
     * The Phone edit text.
     */
    EditText phoneEditText;
    /**
     * The Credit edit text.
     */
    EditText creditEditText;
    /**
     * The Create account button.
     */
    Button createAccountButton;

    AsyncController controller;


    Calendar birthday;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_driver);

        controller = new AsyncController();

        birthday = Calendar.getInstance();
        context = this;
        //defining view objects
        usernameEditText = (EditText) findViewById(R.id.username_add_account_edit_text);
        dobEditText = (EditText) findViewById(R.id.dob_add_account_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_add_account_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_add_account_edit_text);
        creditEditText = (EditText) findViewById(R.id.credit_add_account_edit_text);
        vehicleEditText = (EditText)  findViewById(R.id.driver_add_vehicle);
        createAccountButton = (Button) findViewById(R.id.create_account_button);



        //text formatting listener for phone edit text
        phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog frag = new DatePickerDialog(context, changeBirthday, birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DAY_OF_MONTH));
                frag.show();
            };
        });
        //Create account Button code.
        createAccountButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String creditCard = creditEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String birthdayText = dobEditText.getText().toString().trim();
                String phoneNumber = PhoneNumberUtils.formatNumber(phoneEditText.getText().toString().trim(), "Canada");
                String vehicleDescription = vehicleEditText.getText().toString().trim();
                if(checkForIncorrectEntries(username, birthdayText, email, phoneNumber, creditCard)) {
                    return;
                }

                //make the objects
                Driver user = new Driver(username, birthday.getTime(), creditCard, email, phoneNumber
                , vehicleDescription);

                //check that account doesn't already exist

                User onlineUser = null;
                try{
                    onlineUser = new Gson().fromJson(controller.get("user", "name", user.getName()), User.class);
                    if(onlineUser != null){
                        //if we found another rider with the same name
                        Toast.makeText(AddDriverView.this, "Sorry, that name cannot be used, as it is already in use.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e){
                    Toast.makeText(AddDriverView.this, "Could not communicate with the elastic search server", Toast.LENGTH_SHORT).show();
                    return;
                }

                //successful account creation
                Toast.makeText(AddDriverView.this, "Making Account!", Toast.LENGTH_SHORT).show();
                //save account in elastic search
                try {
                    controller.create("user", user.getID().toString(), new Gson().toJson(user));
                } catch (Exception e){
                    Log.i("Communication Error", "Could not communicate with the elastic search server");
                    return;
                }


                finish();
            }
        });

    }


    private Boolean checkForIncorrectEntries(String formattedName, String birthday, String formattedEmail, String phoneNumber, String creditCard){


        //if the username edit text is empty or hasn't been changed
        if(TextUtils.isEmpty(formattedName)){
            usernameEditText.setError("The Name Field cannot be empty.");
            return true;
        }

        //if the dob edit text is empty or hasn't been changed

        if(TextUtils.isEmpty(birthday)){
            dobEditText.setError("The Date Field cannot be empty. It must be in format YYYY/MM/DD.");
            return true;
        }

        //if the email edit text is empty or hasn't been changed
        if(TextUtils.isEmpty(formattedEmail)){
            emailEditText.setError("The Email Field cannot be empty, " +
                    "and you must provide an email using the pattern john@example.com.");
            return true;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(formattedEmail).matches()){
            //got idea from http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext?noredirect=1&lq=1
            //from answer by user1737884
            emailEditText.setError("A valid email with the pattern john@example.com must be used.");
            return true;
        }

        if(phoneNumber.length() != 14){
            phoneEditText.setError("The Phone Field cannot be empty, and must be of pattern (123) 456-7890, or 1 123-456-7890.");
            return true;
        }

        //gets country code from sim card
        //idea from http://stackoverflow.com/questions/12210696/how-to-get-country-or-its-iso-code from Sahil Mahajan Mj
        if(TextUtils.isEmpty(phoneNumber)) {
            phoneEditText.setError("The Phone Field cannot be empty, and must be of pattern (123) 456-7890, or 1 123-456-7890.");
            return false;
        }
            //if the credit edit text is empty or hasn't been changed
        //I didn't think parsing credit info was important at this moment, but here's how to do it
        //http://stackoverflow.com/questions/11790102/format-credit-card-in-edit-text-in-android

        if(TextUtils.isEmpty(creditCard)){
            creditEditText.setError("The Credit Card Field cannot be empty, and pattern must be exactly XXXXBBBBYYYYAAAA.");
            return true;
        }
        if(creditCard.length() != 16){
            creditEditText.setError("The Credit Card Field must be 16 characters in length, and pattern must be exactly XXXXBBBBYYYYAAAA.");
            return true;
        }
        
        return false;
    }

    /**
     * Listener for when date is changed in a date dialog, sets the dogEditText to the date chosen
     * by the user
     * @return
     */
    private OnDateSetListener changeBirthday = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthday.set(year, month, dayOfMonth);
            SimpleDateFormat date = new SimpleDateFormat("MMM dd yyyy");
            dobEditText.setText(date.format(birthday.getTime()));
        }
    };
}
