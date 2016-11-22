package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.UUID;

public abstract class RequestsFromRidersView extends Activity implements ACallback{
    //must extend activity, not appcompatactivity

    private UUID userID;
    private ArrayList<Request> requests = new ArrayList<>();
    private ListView requestList;
    private RequestController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_from_riders);

        requestList = (ListView)findViewById(R.id.requests_from_riders_list);

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        {
            userID = UUID.fromString(extras.getString("UUID"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(requests != null){
            requests.clear();
        }

        //We need to get the list of requests that has this drivers UUID in their possibleDrivers list
        controller = new RequestController(this);
        controller.findAllRequestsWithDataMember("requests", "possibleDrivers", userID);
        //update gets called from Acallback


        RequestAdapter customAdapter = new RequestAdapter(RequestsFromRidersView.this, requests);
        requestList.setAdapter(customAdapter);

        //this is to recognize listview item presses within the view
        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = requests.get(position);
                String clickedRequestIDStr = request.getID().toString();
                /* Not ready yet, need to wait to merge with the rest of the app,
                so we can figure out how this works
                Intent intent = new Intent(RequestsFromRidersView.this, AcceptDriverView.class);
                intent.putExtra("RequestID", clickedRequestIDStr);
                startActivity(intent);*/
            }
        });


    }

    @Override
    //for Acallback
    public void update() {
        requests.clear();
        requests.addAll(controller.getList());
    }
}
