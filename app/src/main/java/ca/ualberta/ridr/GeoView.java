package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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
public class GeoView extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, ACallback {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Marker> markers;
    private String username;
    private LatLng lastKnownPlace;
    private LatLng restrictToPlace;
    private RequestController requests;
    private boolean firstLoad;
    private boolean test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firstLoad = false;
        requests = new RequestController(this);
        if (mGoogleApiClient == null && !test) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(searchForRequests);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if(extra != null){
            username = extra.getString("username");
        }
    }

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
        lastKnownPlace = getCurrentLocation();
        if(lastKnownPlace != null && !firstLoad) {
            firstLoad = true;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownPlace, 12));
            requests.getAllRequests();
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

        // Allow the user to go home at any time
        map.setMyLocationEnabled(true);

        map.setOnMapLongClickListener(searchAtPoint);
        // What time is it?
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
        map.setOnInfoWindowClickListener(goToRequest);
        map.setInfoWindowAdapter(displayRequest);

    }


    OnMarkerClickListener showInfoWindow = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return false;
        }
    };

    /**
     * Onclick listener to transger to AcceptRiderView
     */
    GoogleMap.OnInfoWindowClickListener goToRequest = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent requestIntent = new Intent(GeoView.this, AcceptRiderView.class);
            Request request = (Request) marker.getTag();
            requestIntent.putExtra("username", username);
            requestIntent.putExtra("RequestUUID", request.getID().toString());
            startActivity(requestIntent);
        }
    };

    /**
     * Allows us to display arbitrary data in the info window of a google marker
     */
    GoogleMap.InfoWindowAdapter displayRequest = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Request currentRequest = (Request) marker.getTag();
            View infoView = getLayoutInflater().inflate(R.layout.info_window_fragment, null);
            TextView pickUpView = (TextView) infoView.findViewById(R.id.pickup);
            TextView dropoffView = (TextView) infoView.findViewById(R.id.dropoff);
            TextView fareView = (TextView) infoView.findViewById(R.id.amount);
            TextView pickupTIme = (TextView) infoView.findViewById(R.id.pickupTime);

            SimpleDateFormat date = new SimpleDateFormat("hh:mm 'on' dd/MM/yyyy");
            pickUpView.setText("Pickup: " + currentRequest.getPickup());
            dropoffView.setText("Drop-off: " + currentRequest.getDropoff());
            pickupTIme.setText("Pickup time: " + date.format(currentRequest.getDate()));
            fareView.setText("Amount: $" + currentRequest.getFare());

            return infoView;
        }
    };

    @Nullable
    /**
     * When called this function checks uses LocationServices to grab the lastLocation and returns
     * that LatLng to the caller
     * @nullable
     * @return currentLocation
     */
    // Simple function to grab current location in LatLong
    private LatLng getCurrentLocation(){
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(currentLocation != null) {
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
        return null;
    }

    /**
     * This function is called when the user uses the Google place widget and selects a place
     * it then centers the camera on the location and gets the request controller to call out to
     * the server and checks for any requests within a 2km range.
     *
     * We are arbitrarily setting a zone, or region to anything thats within 2km of the center point
     */
    PlaceSelectionListener searchForRequests = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 9));
            requests.findAllRequestsWithinDistance(place.getLatLng(), "2");
        }

        @Override
        public void onError(Status status) {
            // TODO: Handle the error.
            Log.i("Places", "An error occurred: " + status);
        }
    };

    /**
     * Callback for an outside Class to get the view to check for new data
     * This interface is used when a controller updates it's data
     * It will call this callback on whoever instantiated that controller
     */
    public void update(){
        if(requests.size() > 0 ){;
            addMarkers(requests.getList());
        };
    }

    /**
     * Add markers to the map.
     * Takes in a list of requests and adds them as maps to the marker and keeps track of the markers
     * place on the map
     * @param filteredReqeusts the filtered reqeusts
     */
    public void addMarkers(ArrayList<Request> filteredReqeusts){
        map.clear();

        if(filteredReqeusts.size() > 0) {
            if(markers == null){
                markers = new ArrayList<>();
            }
            for (Request request : filteredReqeusts) {
                markers.clear();
                Marker newMarker = map.addMarker(new MarkerOptions().position(request.getPickupCoords()).title(request.getPickup()));
                newMarker.setTag(request);
                markers.add((newMarker));
            }
        }
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

    /**
     * This function is used so that we can set the request controller for the View
     * @param requestController
     */
    public void setRequests(RequestController requestController){
        this.requests = requestController;
    }
    public void setTest(boolean test){
        this.test = test;
    }
    public int countMarkers(){
        if(markers != null){
            return markers.size();
        }
        return 0;
    }

    GoogleMap.OnMapLongClickListener searchAtPoint = new GoogleMap.OnMapLongClickListener(){
        @Override
        public void onMapLongClick(LatLng pos){
            if(map != null) {
                // Move camera to long click position
                map.moveCamera(CameraUpdateFactory.newLatLng(pos));
                requests.findAllRequestsWithinDistance(pos, "2");
            }
        }
    };

    public RequestController getRequestController() {
        return requests;
    }
}
