package ca.ualberta.ridr;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RiderRequestsMadeView extends AppCompatActivity {

    private ListView requestsList;
    private Activity activity = this;
    private ArrayAdapter<Request> adapter;
    private ArrayList<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_rider_has_made);

        requestsList = (ListView) findViewById(R.id.rider_requests_listview);
        //just using a non frag element for the listview for now
        adapter = new ArrayAdapter<Request>(activity, R.layout.request_item_nonfrag, requests);
        requestsList.setAdapter(adapter);

        RequestController RC = new RequestController();
        Rider rider = null;
        requests = RC.getRequests(rider);

        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id){
                Toast.makeText(activity,"Clicked an item", Toast.LENGTH_SHORT); //for now , will want popup screen
            }
        });


    }
}
