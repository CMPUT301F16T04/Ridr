package ca.ualberta.ridr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class AcceptRiderView extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView requestFrom;
    private TextView payment;
    private TextView contactInfo;
    private TextView pickupTime;
    private TextView startLocation;
    private TextView endLocation;
    private TextView status;
    private Button acceptRider;

    private UUID driverID;
    private String driverName;
    private UUID requestID;
    private Rider requestRider;
    private Request request;
    private Driver driver;

    private boolean agreedToFulfill = false;

    //gmap variables
    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng lastKnownPlace;
    private boolean firstLoad;
    private ArrayList<Marker> markers;
    private Geocoder geocoder;

    private RequestController requestController = new RequestController();
    private RiderController riderController = new RiderController();
    private DriverController driverController = new DriverController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_rider);

        //set google map stuff
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

        String space = " ";

        //set all the xml elements
        requestFrom = (TextView) findViewById(R.id.request_from);
        payment = (TextView) findViewById(R.id.payment_accept_rider);
        contactInfo = (TextView) findViewById(R.id.contact_info_accept_rider);
        pickupTime = (TextView) findViewById(R.id.pickup_time_accept_rider);
        startLocation = (TextView) findViewById(R.id.start_location_accept_rider);
        endLocation = (TextView) findViewById(R.id.end_location_accept_rider);
        status = (TextView) findViewById(R.id.status_accept_rider);
        acceptRider = (Button) findViewById(R.id.accept_rider_button);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null)
        {
            driverID = UUID.fromString(extras.getString("userUUID"));
            requestID = UUID.fromString(extras.getString("RequestUUID"));
        } else {
            Log.i("Intent Extras Error", "Error getting driver and request ID from extras in AcceptRiderView");
            finish();
        }

        //TODO dont really need to get rider once we fix the names thing
        request = requestController.getRequestFromServer(requestID.toString());
        requestRider = riderController.getRiderFromServer(request.getRider());

        String isFrom = "Request from " + capitalizeName(requestRider.getName()) + ":";

        SimpleDateFormat rideDate = new SimpleDateFormat("HH:mm 'on' dd MMM yyyy");


        //set all the text that needs to be set
        requestFrom.setText(isFrom);
        String paymentText = payment.getText() + space+ Float.toString(request.getFare());
        payment.setText(paymentText);
        String contactInfoText = contactInfo.getText() + space + requestRider.getPhoneNumber();
        contactInfo.setText(contactInfoText);
        String pickupTimeText = pickupTime.getText() + space + rideDate.format(request.getDate());
        pickupTime.setText(pickupTimeText);
        String startLocationText = startLocation.getText() + space + request.getPickup();
        startLocation.setText(startLocationText);
        String endLocationText = endLocation.getText() + space + request.getDropoff();
        endLocation.setText(endLocationText);

        driverName = driverController.getDriverFromServer(driverID.toString()).getName();
        //have to check if the user previously accepted
        checkIfUserAccepted(requestID.toString());

        //display two diff strings based on status of acceptance
        if(agreedToFulfill){
            String statusText = status.getText() + " Accepted";
            status.setText(statusText);
        } else {
            String statusText = status.getText() + " Not accepted";
            status.setText(statusText);
        }

        //make the requestFrom text view clickable
        requestFrom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(AcceptRiderView.this, ProfileView.class);
                intent.putExtra("RiderUUID", requestRider.getID().toString());
                startActivity(intent);
            }
        });

        //when clicking on the accept rider button, we check if we already accepted. If not,
        //add ourselves to list of possibleDrivers in the request
        acceptRider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(agreedToFulfill){
                    //chose this particular toast over "accepted already" so
                    //they dont think that someone accepting prevents their own acceptance
                    Toast.makeText(AcceptRiderView.this, "You've already accepted", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    requestController.addDriverToList(request, driverName);

                    agreedToFulfill = true;
                    String statusText = getResources().getString(R.string.status_accept_rider) + " Accepted";
                    status.setText(statusText);
                }
            }

        });

    }

    //gmap stuff
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
        if(lastKnownPlace != null && !firstLoad) {
            firstLoad = true;
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
        setupMap(request);

    }

    /**
     *  sets up the map by adding the two markers and then zooms
     *  zoom is delegated to zoomToMid function
     *
     * @param request used to set marker points
     */
    //TODO put the route between start and end on the map
    private void setupMap(Request request){

        //adds markers and then move camera to where they are
        Marker startMarker = gMap.addMarker(new MarkerOptions().position(request.getPickupCoords()).title(request.getPickup()));
        startMarker.setTag(request);
        Marker endMarker = gMap.addMarker(new MarkerOptions().position(request.getDropOffCoords()).title(request.getDropoff()));
        endMarker.setTag(request);

        zoomToMid(request);

    }

    /**
     *  builds an area based on the request start and end points and then
     *  zooms in to that area + 120 pixels padding
     *
     * @param request used to obtain points of boundary
     */
    private void zoomToMid(Request request){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(request.getDropOffCoords());
        builder.include(request.getPickupCoords());
        final LatLngBounds bounds = builder.build();

        //for some reason the map wasnt loading before this point sometimes, so lets check first
        gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });
    }

    /**
     * iterate through the requests possibleDriver arraylist, and see if we are in it
     * if we are we will set the flag to true so cannot try to reaccept
     *
     * @param requestId used to find the possible drivers list
     */
    private void checkIfUserAccepted(String requestId) {
        ArrayList<String> drivers = requestController.getPossibleDrivers(requestId);
        for (int i = 0; i < drivers.size(); ++i) {
            if (drivers.get(i).equals(driverName)) {
                agreedToFulfill = true;
                break;
            }
        }
    }

    /** just some formatting, might not be necessary if the names are enforced
     *  to be capitalized but wont hurt to have this til then
     *
     * @param name the possibly lowercased name
     * @return the name with the first letter capitalized
     */
    private String capitalizeName(String name){
        return (name.substring(0,1).toUpperCase().concat(name.substring(1)));
    }


}



