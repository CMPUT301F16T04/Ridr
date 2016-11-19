package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Modified by nkaefer on 2016/11/08
 *
 * This activity displays the login screen, and handles the logic of logging in as a rider or a driver.
 *
 */
public class LoginView extends Activity {
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
                if(TextUtils.isEmpty(usernameLogin.getText().toString())){
                    Toast.makeText(LoginView.this, "Please enter in a username to log in.", Toast.LENGTH_SHORT).show();
                    return;
                }
                User myUser = null;
                try{
                    myUser = new Gson().fromJson(new AsyncController().get("user", "name", usernameLogin.getText().toString().trim()), User.class);
                } catch (Exception e){
                    Toast.makeText(LoginView.this, "Could not communicate with the elastic search server", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        });

        //driver vs rider login logic
        driverLogin.setOnClickListener(changeUserType);
        riderLogin.setOnClickListener(changeUserType);

        //add account intent launcher
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //launches to account creator activity
                Intent addAccountIntent = new Intent(LoginView.this, AddUserView.class);
                startActivity(addAccountIntent);
            }
        });

    }

    private void loginRider(User myUser){
        if(myUser == null){
            return;
        }
        Intent riderScreenIntent = new Intent(LoginView.this, RiderMainView.class);
        riderScreenIntent.putExtra("UUID", myUser.getID().toString());
        System.out.println(myUser.getID().toString());
        startActivity(riderScreenIntent);
    }
    private void loginDriver(User myUser){
        //if our user is logging in as a driver
        if(myUser == null){
            return;
        }
        Intent driverScreenIntent = new Intent(LoginView.this, SearchResultsView.class);
        driverScreenIntent.putExtra("UUID", myUser.getID().toString());
        startActivity(driverScreenIntent);
    }

    /**
     * The Change user type.
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
