package ca.ualberta.ridr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AcceptRiderView extends AppCompatActivity {

    private TextView requestInfo;
    private TextView requestFrom;
    private CharSequence isFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_rider);

        requestInfo = (TextView) findViewById(R.id.possible_request_info);
        requestFrom = (TextView) findViewById(R.id.request_from);

        Request request = null;

        isFrom = "Request From"+ request.getRider();

        requestFrom.setText(isFrom);

        requestFrom.setOnClickListener(View.OnClickListener);
    }
}
