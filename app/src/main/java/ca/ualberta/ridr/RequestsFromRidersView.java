package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

public class RequestsFromRidersView extends Activity {
    //must extend activity, not appcompatactivity

    private String userName;
    private ArrayList<Request> requests = new ArrayList<>();
    private ListView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_from_riders);

        requestList = (ListView)findViewById(R.id.requests_from_riders_list);

        Intent intent= getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        {
            userName = extras.getString("driverName");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(requests != null){
            requests.clear();
        }

        //We need to get the list of requests that has this drivers UUID in their possibleDrivers list
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getFromIndexObjectInArray("requests", "possibleDrivers", userName);

        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }


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
}
