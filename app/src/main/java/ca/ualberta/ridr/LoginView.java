package ca.ualberta.ridr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginView extends Activity {
    TextView riderLogin;
    TextView driverLogin;
    Button loginButton;
    boolean asDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        asDriver = true;
        driverLogin = (TextView) findViewById(R.id.DriverLogin);
        riderLogin = (TextView) findViewById(R.id.RiderLogin);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //System.out.println(new asyncOperations().getAllFromIndexFiltered("user", "id", "AAAAAAAAAAAAAAAAAAAAAAAAAAA"));
                /*if(event.getAction() == MotionEvent.ACTION_DOWN){
                    loginButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.dark_tertiary_colour));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    loginButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.tertiary_colour));
                }*/
                return false;
            }
        });
        driverLogin.setOnClickListener(changeUserType);
        riderLogin.setOnClickListener(changeUserType);


    }


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
