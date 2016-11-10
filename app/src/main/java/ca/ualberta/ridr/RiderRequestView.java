package ca.ualberta.ridr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class RiderRequestView extends AppCompatActivity {
    private ListView oldRequestsList;
    public ArrayList<Request> requestArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_request);

        //Declaring reference buttons in the GUI
        Button menuButton = (Button) (findViewById(R.id.menu_button));
        oldRequestsList = (ListView) (findViewById(R.id.oldRequestLists));

        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                //Open a pop up menu

            }
        });

        oldRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When an item is clicked, Complete it? stuff to be implimented here.
                // not relevant to branch US_01_02_01
            }
        });
    }
    @Override
    protected void onStart(){


    }
}
