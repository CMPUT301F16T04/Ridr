package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class RequestsFromRidersView extends Activity implements ACallback{
    //must extend activity, not appcompatactivity

    private UUID userID;
    private ArrayList<Request> requests = new ArrayList<>();
    private ListView requestList;
    private RequestController requestController;

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
        DriverController driverController = new DriverController();
        Driver myself = driverController.getDriverFromServer(userID.toString());
        requestController = new RequestController(this);
        requestController.findAllRequestsWithDataMember("request", "possibleDrivers", myself.getName());
        //search for our name in any possibleDriver list
        //update gets called from Acallback


        RequestAdapter customAdapter = new RequestAdapter(RequestsFromRidersView.this, requests, myself.getName());
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

        if(requests.size() <= 0){
            Toast.makeText(RequestsFromRidersView.this, "You haven't accepted any requests yet!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    //for Acallback
    public void update() {
        requests.clear();
        requests.addAll(requestController.getList());
    }
}
