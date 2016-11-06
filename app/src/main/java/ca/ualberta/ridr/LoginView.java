package ca.ualberta.ridr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import static android.R.attr.type;

public class LoginView extends Activity {
    TextView riderLogin;
    TextView driverLogin;
    boolean asDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        asDriver = true;
        driverLogin = (TextView) findViewById(R.id.DriverLogin);
        riderLogin = (TextView) findViewById(R.id.RiderLogin);

        driverLogin.setOnClickListener(new View.OnClickListener() {
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
        });
    }
}
