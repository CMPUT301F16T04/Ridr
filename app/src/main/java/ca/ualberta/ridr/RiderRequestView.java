package ca.ualberta.ridr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

public class RiderRequestView extends Activity {
    public ArrayList<Request> requests = new ArrayList<>();
    //Declaring reference buttons in the GUI
    ListView oldRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_requests);
        oldRequestsList = (ListView) (findViewById(R.id.oldRequestLists));

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", "8e16686b-f72d-42e1-90ea-e7a8cf270732");
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject()));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }


        ListAdapter customAdapter = new RiderRequestListAdapter(this, R.layout.rider_request_list_tem, requests);
        oldRequestsList.setAdapter(customAdapter);
    }
}
