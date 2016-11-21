package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
/** The RiderRequestView displays a list of active requests for a logged in rider
 * @author mackenzie
 * */

public class RiderRequestView extends Activity {
    public ArrayList<Request> requests = new ArrayList<>();
    //Declaring reference buttons in the GUI
    ListView oldRequestsList;
    public Rider loggedInRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        oldRequestsList = (ListView) (findViewById(R.id.search_results));


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", loggedInRider.getID().toString());
        requests.clear(); // Fix for duplicates in list
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }
        }
        RequestAdapter customAdapter = new RequestAdapter(this, requests);
        oldRequestsList.setAdapter(customAdapter);

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }
}
