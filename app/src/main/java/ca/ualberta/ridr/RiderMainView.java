package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


/**
 * This view allows for a rider to create a new request
 */
public class RiderMainView extends FragmentActivity implements ACallback, OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener{

    private EditText startLocation;
    private EditText endLocation;
    private EditText fareInput;

    private TextView dateTextView;
    private TextView timeTextView;

    private Button addRequest;
    private Button dateButton;
    private Button timeButton;
    private Button menuButton;

    private UUID currentUUID; // UUID of the currently logged-in rider
    private String currentIDStr; // string of the curretn UUID
    private Rider currentRider;

    private String defaultStartText = "Enter Start Location";
    private String defaultDestinationText = "Enter Destination";

    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng lastKnownPlace;
    private boolean firstLoad;
    private ArrayList<Marker> markers;
    private Geocoder geocoder;

    private Context context = this;

    RequestController reqController;
    //RiderController riderController = new RiderController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rider_main);
        reqController = new RequestController(context);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.newRequestMap);
        mapFragment.getMapAsync(this);
        firstLoad = false;
        geocoder = new Geocoder(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        //retrieve the current rider's UUID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            currentIDStr = extras.getString("UUID");
            currentUUID = UUID.fromString(currentIDStr);
        }
        //from the UUID, get the rider object
        try {
            currentRider = new Gson().fromJson(new AsyncController(context).get("user", "id", currentIDStr), Rider.class);
        } catch(Exception e){
            Log.i("Error parsing Rider", e.toString());
        }


        setViews();
        //TODO afterTextChanged for destination, and start location, get distance and estimate fare
//  work in progress
//        startLocation.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                return;
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                return;
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!endLocation.getText().toString().matches("") || !endLocation.getText().toString().matches(defaultStartText)){
//                    Toast.makeText(RiderMainView.this, "both changed", Toast.LENGTH_SHORT).show();
//                    //end location has also changed, so get a fare estimate
//                    fareInput.setText(String.format("%2f",reqController.getFareEstimate(10))); // using a dumy value of 10 as distance for now
//                } else{
//                    Toast.makeText(RiderMainView.this, "only one changed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        endLocation.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                return;
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                return;
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!startLocation.getText().toString().matches("") || !startLocation.getText().toString().matches(defaultStartText)){
//                    Toast.makeText(RiderMainView.this, "both changed", Toast.LENGTH_SHORT).show();
//                    //end location has also changed, so get a fare estimate
//                    fareInput.setText(String.format("%2f",reqController.getFareEstimate(10))); // using a dumy value of 10 as distance for now
//                }else{
//                    Toast.makeText(RiderMainView.this, "only one changed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //open date picker
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //makes a date fragment when clicked
                DialogFragment frag = new DateSelector();
                frag.show(getFragmentManager(), "DatePicker");
            }
        });

        //open time picker
        timeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // make a date fragment when clicked
                DialogFragment frag = new TimeSelector();
                frag.show(getFragmentManager(), "TimePicker");
            }
        });

        // create request button
        addRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Rider rider = null; // for now just so that we wont get compile errors
                System.out.println(new Gson().toJson(currentRider));
                addRequestEvent(currentRider);
            }
        });

        //menu button
        menuButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showMenu(v);
            }
        });

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    protected void onResume(){
        super.onResume();
        mGoogleApiClient.reconnect();
    }

    protected void onPause(){
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    // Need this for ConnectionsCallback, doesn't need to do anything AFAIK
    // If a map view does live tracking it might be more useful
    public void onConnectionSuspended(int i){

    }

    @Override
    //On connected listener, required to be able to zoom to users location at login
    public void onConnected(Bundle connectionHint){
        //lastKnownPlace = getCurrentLocation();
        if(lastKnownPlace != null && !firstLoad) {
            firstLoad = true;
            //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownPlace, 12));
            gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.5, 133.5)));
        }

    }

    // This should eventually be updated to quit the app or go back to a view that doesn't require geolocation
    // Currently this shows an alert notifying the user that the connection failed
    public void onConnectionFailed(ConnectionResult result) {
        new AlertDialog.Builder(this)
                .setTitle("Connection Failure")
                .setMessage(result.getErrorMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;
    }

    public void update(){}

    /**
     * finds views by their ID's and assigns them to their respective variable
     */
    private void setViews(){
        startLocation = (EditText) findViewById(R.id.editStartLocationText);
        endLocation = (EditText) findViewById(R.id.editEndLocationText);
        fareInput = (EditText) findViewById(R.id.editFare);

        dateTextView = (TextView) findViewById(R.id.dateText);
        timeTextView = (TextView) findViewById(R.id.timeText);

        addRequest = (Button) findViewById(R.id.createRequestButton);
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);
        menuButton = (Button) findViewById(R.id.riderMainMenuButton);
    }

    /**
     * reset text inputs in the view
     */
    private void resetText(){
        startLocation.setText(defaultStartText);
        endLocation.setText(defaultStartText);
        dateTextView.setText("");
        timeTextView.setText("");
    }

    /**
     * handles the event called when Create Request is clicked. Verifies that the fields have been filled and creates a Request
     * @param rider
     */
    private void addRequestEvent(Rider rider){

        if(startLocation.getText().toString().matches("") || startLocation.getText().toString().matches(defaultStartText)){
            Toast.makeText(RiderMainView.this, "Please enter the address from where you would like to be picked up", Toast.LENGTH_SHORT).show();
            return;
        }
        if(endLocation.getText().toString().matches("") || endLocation.getText().toString().matches(defaultDestinationText)){
            Toast.makeText(RiderMainView.this, "Please enter the address of your destination", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dateTextView.getText().toString().matches("")){
            Toast.makeText(RiderMainView.this, "Please enter the date on which you would like to be picked up", Toast.LENGTH_SHORT).show();
            return;
        }
        if(timeTextView.getText().toString().matches("")){
            Toast.makeText(RiderMainView.this, "Please enter the time at which you would like to be picked up", Toast.LENGTH_SHORT).show();
            return;
        }
        String pickupStr = startLocation.getText().toString();
        String dropoffStr = endLocation.getText().toString();
        //LatLng pickupCoord = getLocationFromAddress(pickupStr);
        //LatLng dropoffCoord = getLocationFromAddress(dropoffStr);
        LatLng pickupCoord = new LatLng(53.525288, -113.525454);
        LatLng dropoffCoord =  new LatLng(53.484775, -113.505067);
        Date pickupDate = stringToDate(dateTextView.getText().toString(), timeTextView.getText().toString());
        reqController.createRequest(rider, pickupStr, dropoffStr, pickupCoord, dropoffCoord, pickupDate);
        Toast.makeText(RiderMainView.this, "request made", Toast.LENGTH_SHORT).show();

        // reset text fields
        resetText();
    }

    /**
     * popup menu that allows the user to edit their profile and view all of their requests
     * @param v
     */
    public void showMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.rider_main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                switch(item.getItemId()){
                    case R.id.mainRiderMenuEditUserInfo:
                        Toast.makeText(RiderMainView.this, "Edit User Info", Toast.LENGTH_SHORT).show();
                        resetText();
                        Intent editInfoIntent = new Intent(RiderMainView.this, EditProfileView.class);
                        editInfoIntent.putExtra("UUID", currentIDStr);
                        startActivity(editInfoIntent);
                        return true;
                    case R.id.mainRiderMenuViewRequests:
                        Toast.makeText(RiderMainView.this, "View Requests", Toast.LENGTH_SHORT).show();
                        resetText();
                        Intent viewRequestsIntent = new Intent(RiderMainView.this, RiderRequestView.class);
                        viewRequestsIntent.putExtra("UUID", currentIDStr);
                        startActivity(viewRequestsIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }



    /**
     * converts data and time strings into a Date object
     * @param dateString string with format dd/MM/yyyy
     * @param timeString string with format hh:mm a, where hh is the time from 1-12 and a is an am/pm indicator
     * @return a date object with the format dd/MM/yyyy hh:mm a
     */
    private Date stringToDate(String dateString, String timeString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        try{
            return dateFormat.parse(dateString +" "+ timeString);
        } catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }

//    @Nullable
//    /**
//     * When called this function checks uses LocationServices to grab the lastLocation and returns
//     * that LatLng to the caller
//     * @nullable
//     * @return currentLocation
//     */
//    // Simple function to grab current location in LatLong
//    private LatLng getCurrentLocation(){
//        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if(currentLocation != null) {
//            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        }
//        return null;
//    }

    /**
     * Add markers to the map.
     * Takes in a list of requests and adds them as maps to the marker and keeps track of the markers
     * place on the map
     * @param filteredReqeusts the filtered reqeusts
     */
    public void addMarkers(ArrayList<Request> filteredReqeusts){
        gMap.clear();

        if(filteredReqeusts.size() > 0) {
            if(markers == null){
                markers = new ArrayList<>();
            }
            for (Request request : filteredReqeusts) {
                markers.clear();
                Marker newMarker = gMap.addMarker(new MarkerOptions().position(request.getPickupCoords()).title(request.getPickup()));
                newMarker.setTag(request);
                markers.add((newMarker));
            }
        }
    }

    /**
     * converts a street address into a geo location using geocoder
     * @param addressString a street address
     * @return a LatLng geo location
     */
    private LatLng getLocationFromAddress(String addressString){
        List<Address> addressList;
        LatLng point = null;
        try{
            addressList = geocoder.getFromLocationName(addressString,5);
            if (addressList == null){
                // nothing found
                return null;
            }
            Address location = addressList.get(0);
            location.getLatitude();
            location.getLongitude();

            point = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e){
            e.printStackTrace();

        }
        return point;
    }

}
