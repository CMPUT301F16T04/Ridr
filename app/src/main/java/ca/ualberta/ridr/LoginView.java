package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Modified by nkaefer on 2016/11/08, Modified by Jusitn on November 24, 2016
 *
 * This activity displays the login screen, and handles the logic of logging in as a rider or a driver.
 *
 */
public class LoginView extends Activity implements ACallback {
    AccountController accountController;
    /**
     * The Rider login.
     */
    TextView riderLogin;
    /**
     * The Driver login.
     */
    TextView driverLogin;
    /**
     * The Login button.
     */
    Button loginButton;
    /**
     * The Create account button.
     */
    Button createAccountButton;
    /**
     * The Username login.
     */
    EditText usernameLogin;
    /**
     * The As driver.
     */
    boolean asDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        accountController = new AccountController(this);

        asDriver = true;

        driverLogin = (TextView) findViewById(R.id.DriverLogin);
        riderLogin = (TextView) findViewById(R.id.RiderLogin);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameLogin = (EditText) findViewById(R.id.username_login_edit_text);
        createAccountButton = (Button) findViewById(R.id.add_account_login_button);

        //login button intent launcher
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launches to next activity activity
                //check for empty textbox
                String username = usernameLogin.getText().toString();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginView.this, "Please enter in a username to log in.", Toast.LENGTH_SHORT).show();
                    return;
                }
                accountController.loginUser(username);
            }
        });

        //driver vs rider login logic
        driverLogin.setOnClickListener(changeUserType);
        riderLogin.setOnClickListener(changeUserType);

        //add account intent launcher
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //launches to account creator activity
                Intent addAccountIntent;
                String username = usernameLogin.getText().toString();

                if(asDriver){
                    addAccountIntent = new Intent(LoginView.this, AddDriverView.class);
                } else {
                    addAccountIntent = new Intent(LoginView.this, AddRiderView.class);
                }
                if(!TextUtils.isEmpty(username)) {
                    addAccountIntent.putExtra("username", username);
                }
                startActivity(addAccountIntent);
            }
        });

    }

    /**
     * Checks to see if a user has the rider attribute, and if so logs the user in as a rider
     * if not, the user is asked to add his driver information to his account.
     * @param myUser
     */
    private void loginRider(User myUser){
        if(myUser == null){
            return;
        }
        if(myUser.isRider()) {
            Intent riderScreenIntent = new Intent(LoginView.this, RiderMainView.class);
            riderScreenIntent.putExtra("username", myUser.getName());
            startActivity(riderScreenIntent);
        } else {
            Toast.makeText(LoginView.this, "Sorry, this user is not a rider. Please add your rider info to your account", Toast.LENGTH_LONG).show();
            Intent updateAccount = new Intent(LoginView.this, AddRiderView.class);
            updateAccount.putExtra("username", myUser.getName());
            startActivity(updateAccount);
        }
    }

    /**
     * Checks to see if a user has the driver attribute, and if so logs the user in as a driver
     * if not, the user is asked to add his driver information to his account.
     * @param myUser
     */
    private void loginDriver(User myUser){
        //if our user is logging in as a driver
        if(myUser == null) {
            return;
        }

        if(myUser.isDriver()) {
            Intent driverScreenIntent = new Intent(LoginView.this, SearchResultsView.class);
            driverScreenIntent.putExtra("username", myUser.getName());
            startActivity(driverScreenIntent);
        } else {
            Toast.makeText(LoginView.this, "Sorry, this user is not a driver. Please add your driver info to your account.", Toast.LENGTH_LONG).show();
            Intent updateAccount = new Intent(LoginView.this, AddDriverView.class);
            updateAccount.putExtra("username", myUser.getName());
            startActivity(updateAccount);
        }
    }


    /**
     * An update function for when we get data back from the server
     */
    public void update(){
        User myUser = accountController.getUser();
        if(myUser == null) {
            //if we found another driver with the same name
            Toast.makeText(LoginView.this, "Sorry, that name does not have an account. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        //code for switching between a rider login and a driver login
        if(asDriver){
            loginDriver(myUser);
        } else {
            loginRider(myUser);
        }

    }
    /**
     * Change type of user trying to log on
     */
//switches between user login type
    View.OnClickListener changeUserType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            asDriver = !asDriver;
            if (asDriver) {
                driverLogin.setBackgroundResource(R.drawable.selected_login_button);
                driverLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
                riderLogin.setBackgroundResource(R.drawable.unselected_login_button);
                riderLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_tertiary_colour));
            } else {
                driverLogin.setBackgroundResource(R.drawable.unselected_login_button);
                driverLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_tertiary_colour));
                riderLogin.setBackgroundResource(R.drawable.selected_login_button);
                riderLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
            }
        }
    };
}
