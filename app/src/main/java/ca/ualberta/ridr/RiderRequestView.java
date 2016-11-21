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
import java.util.Date;


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

            //grab riders uuid

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                currentIDStr = extras.getString("UUID");
                currentUUID = UUID.fromString(currentIDStr);
            }

            AsyncController controller = new AsyncController();
            JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", "726a1db2-1424-4b82-b85d-6968396dcd4a"); //"8e16686b-f72d-42e1-90ea-e7a8cf270732"
            requests.clear();

            System.out.println(queryResults);
            for (JsonElement result : queryResults) {
                try {
                    requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
                } catch (Exception e) {
                    Log.i("Error parsing requests", e.toString());
                }
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

                public void onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                    Request request = requests.get(position);
                    clickedRequestIDStr = request.getID().toString();
                    cancelRequest(request);
                }
            });



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

            //reason this is fudge is because we dont have that list of possible drivers stored
            final ArrayList<String> possibleDrivers = new ArrayList<>();
            final ArrayList<String> possibleDriversIds = new ArrayList<>();
            Driver driver = new Driver("Sample hardcoded driver", new Date(), "creditcard", "email@emailme.email", "123-123-1234", "banckaccono");
            possibleDrivers.add(driver.getName());
            possibleDriversIds.add("475a3caa-88b5-46b2-9a44-cd02ef8a2d28");


            ListView popupList = (ListView) layout.findViewById(R.id.drivers_list);
            ArrayAdapter<String> adapter_popup = new ArrayAdapter<String>(activity, R.layout.driver_who_accepted, possibleDrivers);
            popupList.setAdapter(adapter_popup);

            //this is to recognize listview item presses within the popup
            popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    String riderId = "8e16686b-f72d-42e1-90ea-e7a8cf270732"; //"6a5f339c-2679-4e18-825f-2d6fc6cdc3e2";
//                    String driverId = "475a3caa-88b5-46b2-9a44-cd02ef8a2d28";
//                    String requestId = "4d08b0e5-9bf7-45fb-b5ea-37a5cb03eeba";

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

    public void cancelRequest(Request request) {

        // Inflate the popup_layout.xml
        LinearLayout viewCancelGroup = (LinearLayout) findViewById(R.id.cancel_request);
        LayoutInflater layoutCancelInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cancelLayout = layoutCancelInflater.inflate(R.layout.cancel_request, viewCancelGroup);

        // Creating the PopupWindow
        final PopupWindow cancelPopUp = new PopupWindow(activity);
        cancelPopUp.setContentView(cancelLayout);
        cancelPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int CANCEL_OFFSET_X = 60;
        int CANCEL_OFFSET_Y = 600;

        // Displaying the popup at the specified location, + offsets.
        cancelPopUp.showAtLocation(cancelLayout, Gravity.NO_GRAVITY, CANCEL_OFFSET_X, CANCEL_OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button cancelClose = (Button) cancelLayout.findViewById(R.id.Exit_Cancel_Request);
        Button cancelRequest = (Button) cancelLayout.findViewById(R.id.Confirm_Cancel);

        cancelClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPopUp.dismiss();
            }
        });

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do stuff
            }
        });

    }
}

