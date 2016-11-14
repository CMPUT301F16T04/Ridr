package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static ca.ualberta.ridr.R.layout.profile;

public class RiderRequestsMadeView extends Activity {

    private ListView requestsList;
    private Activity activity = this;
    private ArrayAdapter<Request> adapter;
    private ArrayList<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests_rider_has_made);

        Rider rider = new Rider("name",new Date(), "credit", "email", "phone");
        requests = new ArrayList<>();
        LatLng coords = new LatLng(0,0);
        Request r1 = new Request(rider, "pickup", "dropoff", coords, coords, new Date());
        requests.add(r1);

        requestsList = (ListView) findViewById(R.id.rider_requests_listview);
        //just using a non frag element for the listview for now
        adapter = new ArrayAdapter<>(activity, R.layout.request_item_nonfrag, requests);
        requestsList.setAdapter(adapter);

        //RiderController rideCon = new RiderController();
        //need to get actual Rider at this point
        //requests = rideCon.getRequests(rider);


        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id){
                Toast toast = Toast.makeText(activity,"Clicked on a request!", Toast.LENGTH_SHORT); //for now , will want popup screen
                toast.setMargin(50,50);
                toast.show();
                Request request = requests.get(position);
                displayDrivers(request);
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
        int OFFSET_X = 125;
        int OFFSET_Y = 600;

        // Clear the default translucent background
        //dont think this works since its deprecated
        //need to find way to make popup non translucent
        driverPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        driverPopUp.showAtLocation(layout, Gravity.NO_GRAVITY,  OFFSET_X,  OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.exit_popup);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                driverPopUp.dismiss();
            }
        });

        //RequestController RC = new RequestController();
        ArrayList<Driver> possibleDrivers =  new ArrayList<>();
        Driver driver =  new Driver("name", new Date(), "","","","");
        possibleDrivers.add(driver);
        //RC.getPossibleDrivers(request);


        ListView popup_list = (ListView) layout.findViewById(R.id.drivers_list);
        ArrayAdapter<Driver> adapter_popup = new ArrayAdapter<Driver>(activity, R.layout.driver_who_accepted, possibleDrivers);
        popup_list.setAdapter(adapter_popup);


        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position ,long id){
                Toast toast = Toast.makeText(activity,"Clicked on a driver!", Toast.LENGTH_SHORT); //for now , will want popup screen
                toast.setMargin(50,50);
                toast.show();
                String riderId = "6a5f339c-2679-4e18-825f-2d6fc6cdc3e2";
                String driverId = "475a3caa-88b5-46b2-9a44-cd02ef8a2d28";
                String requestId = "4d08b0e5-9bf7-45fb-b5ea-37a5cb03eeba";
                //TODO pass the driver at clicked position to the next activity
                Intent intent = new Intent(activity, AcceptDriverView.class);
                ArrayList<String> ids = new ArrayList<>();
                ids.add(riderId);
                ids.add(driverId);
                ids.add(requestId);
                intent.putStringArrayListExtra("ids", ids );
                startActivity(intent);
                //go to driver profile

            }

        });
}
}
