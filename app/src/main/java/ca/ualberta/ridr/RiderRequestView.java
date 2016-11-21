package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.UUID;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;


/** The RiderRequestView displays a list of active requests for a logged in rider
 * @author mackenzie
 * */

public class RiderRequestView extends Activity {

    private UUID currentUUID; // UUID of the currently logged-in rider
    private String currentIDStr; // string of the curretn UUID
    private String clickedDriverIDStr; //string of driver who is clicked in popup
    private String clickedRequestIDStr; //string of request that is clicked in listview
    private Activity activity = this;
    public ArrayList<Request> requests = new ArrayList<>();
    //Declaring reference buttons in the GUI
    ListView oldRequestsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_request_view);
        oldRequestsList = (ListView) (findViewById(R.id.oldRequestLists));


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            currentIDStr = extras.getString("UUID");
            currentUUID = UUID.fromString(currentIDStr);
        }
    
        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", currentIDStr);
        requests.clear(); // Fix for duplicates in list
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }


            RequestAdapter customAdapter = new RequestAdapter(activity, requests);
            oldRequestsList.setAdapter(customAdapter);

            //this is to recognize listview item presses within the view
            oldRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Request request = requests.get(position);
                    clickedRequestIDStr = request.getID().toString();
                    displayDrivers(request);
                }
            });
            
        }
    }


    //thinking of popup window as outlined in http://stackoverflow.com/questions/15153651/set-own-layout-in-popup-window-in-android
    //date link accessed : Nov 5 2016
    //author: Emil Adz ,edited Vladimir Kulyk
    public void displayDrivers(Request request) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.drivers_who_accepted);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.drivers_who_accepted, viewGroup);

        // Creating the PopupWindow
        final PopupWindow driverPopUp = new PopupWindow(activity);
        driverPopUp.setContentView(layout);
        driverPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        driverPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        driverPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 60;
        int OFFSET_Y = 600;


        // Displaying the popup at the specified location, + offsets.
        driverPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.exit_popup);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                driverPopUp.dismiss();
            }
        });


        RequestController reqCon = new RequestController();
        DriverController driverCon = new DriverController();
        final ArrayList<String> possibleDrivers = new ArrayList<>();
        final ArrayList<String> possibleDriversIds = reqCon.getPossibleDrivers(clickedRequestIDStr);
        // one last for now fix to fix the fact that we store id's and i want names
        //lots of hitting server tho so later should fix this....
        for (int i = 0; i < possibleDriversIds.size(); i++) {
            possibleDrivers.add(driverCon.getDriverFromServer(possibleDriversIds.get(i)).getName());
        }


        ListView popupList = (ListView) layout.findViewById(R.id.drivers_list);
        ArrayAdapter<String> adapter_popup = new ArrayAdapter<String>(activity, R.layout.driver_who_accepted, possibleDrivers);
        popupList.setAdapter(adapter_popup);

        //this is to recognize listview item presses within the popup
        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                clickedDriverIDStr = possibleDriversIds.get(position);

                //must close popup before going to next activity
                driverPopUp.dismiss();

                //TODO pass the driver at clicked position to the next activity
                Intent intent = new Intent(activity, AcceptDriverView.class);
                ArrayList<String> ids = new ArrayList<>();
                ids.add(currentIDStr); //pass the current user
                ids.add(clickedDriverIDStr);
                ids.add(clickedRequestIDStr);
                intent.putStringArrayListExtra("ids", ids);
                startActivity(intent);
                //go to driver profile
            }
        });


    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


    }
}

