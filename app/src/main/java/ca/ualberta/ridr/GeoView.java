package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class GeoView extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Request request;
    private UUID userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        request = new Request("University of Alberta", "Home", new LatLng(53.525288, -113.525454), new LatLng(53.4848, -113.5051));
        // Create a connection to the GooglePlay api client
        this.userID = UUID.fromString(getIntent().getStringExtra("userID"));


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onResume(){
        super.onResume();
        mGoogleApiClient.reconnect();
        update();
    }

    protected void onPause(){
        mGoogleApiClient.disconnect();
        super.onPause();
    }
    private void update(){

    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    
    // This should eventually be updated to quit the app or go back to a view that doesn't require geolocation
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
    //Basic thing to do when the map is setup
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Calendar current = Calendar.getInstance();
        map.setMyLocationEnabled(true);

        // This method is not needed for loading requests, but demonstrates how to drop a marker
        map.setOnMapLongClickListener(addMarker);
        map.addMarker(new MarkerOptions().position(request.getPickupPos()));
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        if(nightTime(time.format(current.getTime()))) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_night_style);
            map.setMapStyle(style);
        }
    }

    @Override
    // Need this for ConnectionsCallback, doesn't need to do anything AFAIK
    public void onConnectionSuspended(int i){

    }
    @Override
    //On connected listener, required to be able to zoom to users location at login
    public void onConnected(Bundle connectionHint){
        System.out.println("Connected");
        LatLng lastLocation = getCurrentLocation();
        if(lastLocation != null && map != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12));
        }
    }

    @Nullable
    // Simple function to grab current location in LatLong
    private LatLng getCurrentLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(currentLocation != null) {
            System.out.println("grabbing current location succesful");
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        System.out.println("Grabbing current location failed");
        return null;
    }

    // OnLongClickListener for a marker
    GoogleMap.OnMapLongClickListener addMarker = new GoogleMap.OnMapLongClickListener(){
        @Override
        public void onMapLongClick(LatLng pos){
            if(map != null) {
                // Move camera to long click position
                map.moveCamera(CameraUpdateFactory.newLatLng(pos));

                // Drop marker at location
                map.addMarker(new MarkerOptions().position(pos));
            }
        }
    };

    // Let's be fancy and add night time viewing
    // Plus this is easier on the eyes in night
    public boolean  nightTime(String time){
        try {
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(time);
            Date nightTime = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
            return currentTime.getTime() > nightTime.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }

}