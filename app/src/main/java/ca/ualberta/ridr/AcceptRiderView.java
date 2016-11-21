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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
    private UUID requestID;
    private Rider requestRider;
    private Driver driver;
    private Request request;

    private boolean agreedToFulfill;

    //gmap variables
    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng lastKnownPlace;
    private boolean firstLoad;
    private ArrayList<Marker> markers;
    private Geocoder geocoder;

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
        final RequestController requestController = new RequestController();
        request = requestController.getRequestFromServer(requestID.toString());

        //got request, now need to get the rider of the request, since we aren't storing names in the request
        RiderController riderController = new RiderController();
        requestRider = riderController.getRiderFromServer(request.getRider());

        String isFrom = "Request From " + requestRider.getName() + ":";


        //set all the text that needs to be set
        requestFrom.setText(isFrom);
        //set payment field text
        String paymentText = payment.getText() + Float.toString(request.getFare());
        payment.setText(paymentText);
        //set contact info text
        String contactInfoText = contactInfo.getText() + requestRider.getPhoneNumber();
        contactInfo.setText(contactInfoText);
        //set pickup time text
        String pickupTimeText = pickupTime.getText() + request.getDate().toString();
        pickupTime.setText(pickupTimeText);
        //set start location text
        String startLocationText = startLocation.getText() + request.getPickup();
        startLocation.setText(startLocationText);
        //set end location text
        String endLocationText = endLocation.getText() + request.getDropoff();
        endLocation.setText(endLocationText);
        //set status text
        //iterate through the requests possibleDriver arraylist, and see if we are in it. If we are,
        //status = Agreed to fulfill. If not, status = Haven't agreed to fulfill
        agreedToFulfill = false;
        /*for(int i = 0; i < request.getPossibleDrivers().size(); ++i){
            if(request.getPossibleDrivers().get(i).getID().equals(driverID)){
                agreedToFulfill = true;
                break;
            }
        }*/
        if(agreedToFulfill){
            String statusText = status.getText() + "Agreed to fulfill if chosen";
            status.setText(statusText);
        } else {
            String statusText = status.getText() + "Haven't yet agreed to fulfill";
            status.setText(statusText);
        }

        //make the requestFrom text view clickable
        requestFrom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //TODO make ProfileView Activity
                Intent intent = new Intent(AcceptRiderView.this, ProfileView.class);
                intent.putExtra("RiderUUID", requestRider.getID().toString());
                startActivity(intent);
            }
        });

        //when clicking on the accept rider button, we check if we already accepted. If not,
        //add ourselves to list of prospectiveDrivers in the request (? maybe, still haven't got clearance from group)
        acceptRider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(agreedToFulfill){
                    Toast.makeText(AcceptRiderView.this, "Already agreed to fulfill", Toast.LENGTH_SHORT).show();
                    return;
                }


                // things that need to happen if driver accepts the request
                //the drivers list of accepted requests is updated? only if we decide we care to tho
                // the requests list of possible drivers is updatee
                //DC.acceptRequest(driver, request);
                requestController.addDriver(request, driver);



                agreedToFulfill = true;
                String statusText = getResources().getString(R.string.status_accept_rider) + "Agreed to fulfill if chosen";
                status.setText(statusText);
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
        setupMap(request);
    }

    public void setupMap(Request request){
        //gMap.clear();
        //adds markers and then move camera to where they are
        Marker startMarker = gMap.addMarker(new MarkerOptions().position(request.getPickupCoords()).title(request.getPickup()));
        startMarker.setTag(request);
        Marker endMarker = gMap.addMarker(new MarkerOptions().position(request.getDropOffCoords()).title(request.getDropoff()));
        endMarker.setTag(request);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(request.getDropOffCoords(), 10));
        //probably shouldnt hardcode zoom factor, it should be relative to the markers...
        //TODO fix that
        gMap.addPolyline(new PolylineOptions().geodesic(true).add(request.getPickupCoords()).add(request.getDropOffCoords()));


    }


}
