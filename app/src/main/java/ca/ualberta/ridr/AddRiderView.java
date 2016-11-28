package ca.ualberta.ridr;

        import android.app.Activity;
        import android.app.DatePickerDialog;
        import android.content.Context;
        import android.content.Intent;
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

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
/**
 * Modified by nkaefer on 2016/11/08, further modified by Justin Nov 24, 2016
 *
 * This activity displays the add user screen, and handles the text input logic, and parsing of data.
 * Also checks to see if the user we are attempting to make is already in the database, and adds the
 * user if the user is valid.
 */
public class AddRiderView extends Activity implements ACallback {

    private String username;
    private AccountController accountController;
    private Boolean addAccount;
    private Boolean updateUser;
    private Rider newRider;
    private EditText vehicleEditText;
    private AsyncController controller;

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




    private Calendar birthday;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rider);
        accountController = new AccountController(this);
        controller = new AsyncController();
        addAccount = false;
        updateUser =false;
        birthday = Calendar.getInstance();
        context = this;

        //defining view objects
        usernameEditText = (EditText) findViewById(R.id.username_add_account_edit_text);
        dobEditText = (EditText) findViewById(R.id.dob_add_account_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_add_account_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_add_account_edit_text);
        creditEditText = (EditText) findViewById(R.id.credit_add_account_edit_text);
        createAccountButton = (Button) findViewById(R.id.create_account_button);

        phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // We're getting passed in here from another view, let's see if we update a user
            username = extras.getString("username");
            updateUser = true;
            accountController.loginUser(username);
        }
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
                String phoneNumber = PhoneNumberUtils.formatNumber(phoneEditText.getText().toString().trim());

                if(checkForBadInformation(username, birthdayText, email, phoneNumber, creditCard)) {
                    return;
                }

                //make the objects
                if(newRider == null) {
                    newRider = new Rider(username, birthday.getTime(), creditCard, email, phoneNumber);
                }
                addAccount = true;
                //check that account doesn't already exist
                accountController.loginUser(username);
            }
        });

    }


    private Boolean checkForBadInformation(String username, String birthday, String email, String phoneNumber, String creditCard){
        //if the username edit text is empty or hasn't been changed
        if(badUserame(username)){
            return true;
        }
        //if the dob edit text is empty or hasn't been changed
        if(badBirthday(birthday)){
            return true;
        }
        if(badEmail(email)){
            return true;
        }

        if(badPhoneNumber(phoneNumber)){
            return true;
        }

        if(badCreditCart(creditCard)){
            return true;
        }
        return false;
    }

    // Validation of data entry

    private boolean badUserame(String username){
        if(TextUtils.isEmpty(username)){
            usernameEditText.setError("The Name Field cannot be empty.");
            return true;
        }
        return false;
    }
    private boolean badBirthday(String birthday){
        if(TextUtils.isEmpty(birthday)){
            dobEditText.setError("The Date Field cannot be empty. It must be in format YYYY/MM/DD.");
            return true;
        }
        return false;
    }
    private boolean badEmail(String email){
        //if the email edit text is empty or hasn't been changed
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("The Email Field cannot be empty, " +
                    "and you must provide an email using the pattern john@example.com.");
            return true;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //got idea from http://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext?noredirect=1&lq=1
            //from answer by user1737884
            emailEditText.setError("A valid email with the pattern john@example.com must be used.");
            return true;
        }

        return false;
    }

    private boolean badPhoneNumber(String phoneNumber){
        if(phoneNumber.length() != 14){
            phoneEditText.setError("The Phone Field cannot be empty, and must be of pattern (123) 456-7890, or 1 123-456-7890.");
            return true;
        }

        //gets country code from sim card
        //idea from http://stackoverflow.com/questions/12210696/how-to-get-country-or-its-iso-code from Sahil Mahajan Mj
        if(TextUtils.isEmpty(phoneNumber)) {
            phoneEditText.setError("The Phone Field cannot be empty, and must be of pattern (123) 456-7890, or 1 123-456-7890.");
            return true;
        }
        return false;
    }

    private boolean badCreditCart(String creditCard){
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
    private DatePickerDialog.OnDateSetListener changeBirthday = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthday.set(year, month, dayOfMonth);
            SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
            dobEditText.setText(date.format(birthday.getTime()));
        }
    };

    private void createAccount(){
        System.out.println("creating account");
        try {
            controller.create("user", newRider.getID().toString(), new Gson().toJson(newRider));
            //successful account creation
            Toast.makeText(AddRiderView.this, "Account made!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Log.i("Communication Error", "Could not communicate with the elastic search server");
            return;
        }
        finish();
    }

    private void addUserInfoToField(User user){
        System.out.println(new Gson().toJson(user));
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        usernameEditText.setText(user.getName());
        dobEditText.setText(date.format(user.getDateOfBirth()));
        emailEditText.setText(user.getEmail());
        phoneEditText.setText(user.getPhoneNumber());
        creditEditText.setText(user.getCreditCard());
    }

    public void update(){
        // This is messy because my observables are really lazy
        User currentUser = accountController.getUser();
        // this is very hacky. But let's check what state we're in.
        if(!updateUser){
            try{
                if(currentUser != null){
                    //if we found another rider with the same name
                    Toast.makeText(AddRiderView.this, "Sorry, that name cannot be used, as it is already in use.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e){
                Toast.makeText(AddRiderView.this, "Could not communicate with the elastic search server", Toast.LENGTH_SHORT).show();
                return;
            }
            if(addAccount) {

                //save account in elastic search
                createAccount();
            }
        } else {
            // We're updating a user to become a driver

            if(currentUser != null) {
                if(addAccount){
                    newRider.setRiderStatus(true);
                    createAccount();
                } else{
                    Log.i("Get user", "logging in user " + currentUser.getName());
                    // Dangerous, but should copy current user into a driver object and then all we
                    // need to do is update vehicle info and driver status
                    newRider = new Rider(currentUser);
                    addUserInfoToField(currentUser);
                }
            } else {
                addAccount = false;
                updateUser = false;
            }

        }
    }
}
