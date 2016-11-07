package ca.ualberta.ridr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class RiderRequestView extends AppCompatActivity {
    private ListView oldRequestsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_request);

        Button menuButton = (Button)(findViewById(R.id.menu_button));
        oldRequestsList = (ListView)(findViewById(R.id.oldRequestLists));

        menuButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setResult(RESULT_OK);
                //Open a pop up menu

            }
        });
    }

    protected void onStart(){
        //populate list
    }


}
