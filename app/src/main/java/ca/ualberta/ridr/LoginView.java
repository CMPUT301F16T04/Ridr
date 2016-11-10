package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Modified by nkaefer on 2016/11/08
 */
public class LoginView extends Activity {
    TextView riderLogin;
    TextView driverLogin;
    Button loginButton;
    Button createAccountButton;
    EditText usernameLogin;
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

        //login button on touch logic
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    loginButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.dark_tertiary_colour));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    loginButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
                }
                return false;
            }
        });

        //login button intent launcher
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launches to next activity activity
                //check for empty textbox

                if (TextUtils.isEmpty(usernameLogin.getText().toString())) {
                    Toast.makeText(LoginView.this, "Please enter in a username to log in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //code for switching between a rider login and a driver login
                if (asDriver) {
                    Intent driverScreenIntent = new Intent(LoginView.this, RequestsFromRidersView.class);
                    DriverController.GetDriverByNameTask getDriverByNameTask = new DriverController.GetDriverByNameTask();
                    try {
                        Driver myDriver = getDriverByNameTask.execute(usernameLogin.getText().toString().trim()).get();
                        //not really asynchronous anymore, could potentially fix with a loading bar
                        if (myDriver == null) {
                            //if we found another driver with the same name
                            Toast.makeText(LoginView.this, "Sorry, that name does not have an account. Try again.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            driverScreenIntent.putExtra("UUID", myDriver.getUUID().toString());
                            startActivity(driverScreenIntent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginView.this, "Could not communicate with the elastic search server", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Intent riderScreenIntent = new Intent(LoginView.this, RiderMainView.class);
                    RiderController.GetRiderByNameTask getRiderByNameTask = new RiderController.GetRiderByNameTask();
                    try {
                        Rider myRider = getRiderByNameTask.execute(usernameLogin.getText().toString().trim()).get();
                        //not really asynchronous anymore, could potentially fix with a loading bar
                        if (myRider == null) {
                            //if we found another driver with the same name
                            Toast.makeText(LoginView.this, "Sorry, that name does not have an account. Try again.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            riderScreenIntent.putExtra("UUID", myRider.getUUID().toString());
                            startActivity(riderScreenIntent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginView.this, "Could not communicate with the elastic search server", Toast.LENGTH_SHORT).show();
                        return;
                    }
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

    //switches between user login type
    View.OnClickListener changeUserType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            asDriver = !asDriver;
            if (asDriver) {
                driverLogin.setBackgroundResource(R.drawable.selected_login_button);
                driverLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
                riderLogin.setBackgroundResource(R.drawable.login_button_border);
                riderLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_tertiary_colour));
            } else {
                driverLogin.setBackgroundResource(R.drawable.login_button_border);
                driverLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_tertiary_colour));
                riderLogin.setBackgroundResource(R.drawable.selected_login_button);
                riderLogin.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
            }
        }
    };
}
