package ca.ualberta.ridr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
        setContentView(R.layout.search_results);
        oldRequestsList = (ListView) (findViewById(R.id.search_results));

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", "8e16686b-f72d-42e1-90ea-e7a8cf270732");
        System.out.println(queryResults);
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
}
