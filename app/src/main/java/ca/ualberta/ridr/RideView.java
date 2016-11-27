package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * This view shows all requests by default, and then upon user input shows the user all requests
 * centered around the input in a 2km radius
 */
public class RideView extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, ACallback {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Marker> markers;
    private String user;
    private LatLng lastKnownPlace;
    private LatLng restrictToPlace;
    private RideController rides;
    private String rideID;
    private Button info;
    private Context context;
    private boolean firstLoad;
    private boolean test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.ride_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        info = (Button) findViewById(R.id.accept_rider_button);
        firstLoad = false;
        rides = new RideController(this);
        if (mGoogleApiClient == null && !test) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideUp();
            }
        });
        markers = new ArrayList<>();
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if(extra != null){
            user = extra.getString("username");
            rideID = extra.getString("rideID");
        }
        rideID = "513eb9ed-9c45-4468-97ae-73cdcfe5619a";
    }

    OnMarkerClickListener showInfoWindow = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return true;
        }
    };

    protected void onStart() {
        if(!test){
            mGoogleApiClient.connect();
        }

        super.onStart();
    }
    protected void onResume(){

        super.onResume();
        if(!test) {
            mGoogleApiClient.reconnect();
        }
    }

    protected void onPause(){
        if(!test) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    protected void onStop() {
        if(!test) {
            mGoogleApiClient.disconnect();
        }
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
        if(rideID != null) {
            rides.findRide(rideID);
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
    // Basic thing to do when the map is setup
    // Callback for when the googleMap object is set up
    public void onMapReady(GoogleMap googleMap) {
        // Make our map a map
        map = googleMap;

        Calendar current = Calendar.getInstance();

        // Add night view for nice viewing when it's dark out
        // Dark styling is easier on the eyes
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        if (nightTime(time.format(current.getTime()))) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_night_style);
            map.setMapStyle(style);
        }

        // Let's listen for clicks on our markers to display information
        map.setOnMarkerClickListener(showInfoWindow);

//        map.setInfoWindowAdapter(displayRequest);

    }

    //    http://stackoverflow.com/questions/31394829/how-to-disable-button-while-alphaanimation-running
// How to disable a button during animations
    private void slideUp(){
        LinearLayout hiddenPanel = (LinearLayout)findViewById(R.id.rideInfo);
        if(hiddenPanel.getVisibility() == View.VISIBLE){
            Animation topDown = AnimationUtils.loadAnimation(context,
                    R.anim.slide_to_bottom);
            topDown.setAnimationListener(listener);
            hiddenPanel.startAnimation(topDown);
            hiddenPanel.setVisibility(View.INVISIBLE);

        } else {
            Animation bottomUp = AnimationUtils.loadAnimation(context,
                    R.anim.slide_from_bottom);
            bottomUp.setAnimationListener(listener);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
    }

    Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            info.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            info.setClickable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };


    public void updateRideInfo(Ride ride){
        TextView rideCompleted = (TextView) findViewById(R.id.rideCompleted);
        TextView ridePickup = (TextView) findViewById(R.id.ridePickup);
        TextView rideDropoff = (TextView) findViewById(R.id.rideDropoff);
        TextView riderName = (TextView) findViewById(R.id.riderName);
        TextView ridePickupTime = (TextView) findViewById(R.id.ridePickupTime);
        TextView rideFare = (TextView) findViewById(R.id.rideFare);

        SimpleDateFormat rideDate = new SimpleDateFormat("HH:mm 'on' dd MMM yyyy");

        //String fareText = ride.isPaid()? "Fare(Paid): $": "Fair: $";
        //String completed = ride.isCompleted().toString();
        String completed = "completed";
        rideCompleted.setText("Completed: " + completed.substring(0, 1).toUpperCase() + completed.substring(1));
        riderName.setText("Rider: " + ride.getRider());
        ridePickup.setText("Pickup: " + ride.getPickupAddress());
        ridePickupTime.setText("Time: " + rideDate.format(ride.getRideDate()));
        rideDropoff.setText("Drop off: " + ride.getDropOffAddress());
        rideFare.setText("Is paid" + Double.toString(ride.getFare()));

    }
    /**
     * Callback for an outside Class to get the view to check for new data
     * This interface is used when a controller updates it's data
     * It will call this callback on whoever instantiated that controller
     */
    public void update(){
        try {
            Ride ride = rides.getRide(rideID);
            showRide(ride);
        } catch (Exception e){
            Log.i("Update failed", String.valueOf(e));
        }
    }

    public void showRide(Ride ride){
        Marker pickup = map.addMarker(new MarkerOptions().position(ride.getPickupCoords()).title(ride.getPickupAddress()));
        //pickup.setTitle(ride.getPickupAddress());
        markers.add(pickup);

        Marker dropoff = map.addMarker(new MarkerOptions().position(ride.getDropOffCoords()).title(ride.getDropOffAddress()));
        pickup.setTitle(ride.getDropOffAddress());
        markers.add(dropoff);

        // Constrain map
        LatLngBounds.Builder boundedMap = new LatLngBounds.Builder();
        boundedMap.include(ride.getPickupCoords());
        boundedMap.include(ride.getDropOffCoords());

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(boundedMap.build(), 200));

        updateRideInfo(ride);
    }

    /**
     * A function to check if it's night time or not
     * Plus this is easier on the eyes in night
     * @param time the time
     * @return boolean
     */
    private boolean nightTime(String time){
        try {
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(time);
            Date nightTime = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
            Date earlyMorning = new SimpleDateFormat("HH:mm:ss").parse("6:00:00");
            return currentTime.getTime() > nightTime.getTime() || currentTime.getTime() < earlyMorning.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public int countMarkers(){
        if(markers != null){
            return markers.size();
        }
        return 0;
    }
}
