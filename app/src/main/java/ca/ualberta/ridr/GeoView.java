package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.widget.Toast.makeText;

public class GeoView extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Request request;
    private UUID userID;
    private LatLng lastKnownPlace;
    private LatLng restrictToPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        request = new Request(rider, "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        // Create a connection to the GooglePlay api client
        //this.userID = UUID.fromString(getIntent().getStringExtra("userID"));

        if (mGoogleApiClient == null) {
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

    @Override
    // Need this for ConnectionsCallback, doesn't need to do anything AFAIK
    public void onConnectionSuspended(int i){

    }
    @Override
    //On connected listener, required to be able to zoom to users location at login
    public void onConnected(Bundle connectionHint){
        System.out.println("Connected");
        lastKnownPlace = getCurrentLocation();
        if(lastKnownPlace != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownPlace, 12));
        }
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


        map.addMarker(new MarkerOptions().position(request.getPickupCoords()).title(request.getPickup())).setTag(request);

        // Add night view for nice viewing when it's dark out
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        if (nightTime(time.format(current.getTime()))) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_night_style);
            map.setMapStyle(style);
        }

        map.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

                pickUpView.setText("Pickup: " + currentRequest.getPickup());
                dropoffView.setText("Drop-off: " + currentRequest.getDropoff());
                fareView.setText("Amount: " + "$20");

                return infoView;
            }
        });

        System.out.println("Creating test data");
        AsyncController controller = new AsyncController();

        JsonArray results = controller.getAllFromIndex("request");
        JsonObject driverResults = controller.get("user", "name", "Justin Barclay");
        Rider rider = new Gson().fromJson(controller.get("user", "name", "Justin Barclay"), Rider.class);

        Request request = new Request(rider, "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.50505), new Date() );

        System.out.println(controller.create("request", request.getID().toString(), request.toJson()));

        for(JsonElement item : results){
            try{
//                Request test;
//                System.out.println(item.getAsJsonObject().getAsJsonObject("_source"));
//                test = new Request(item.getAsJsonObject().getAsJsonObject("_source"));
//                System.out.println(new Gson().toJson(test));
            } catch (Exception e){
                Log.d("Error parsing request", e.toString());
            }
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

    PlaceSelectionListener searchForRequests = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            // TODO: Get info about the selected place.

            Log.i("Places", "Place: " + place.getName());
        }

        @Override
        public void onError(Status status) {
            // TODO: Handle the error.
            Log.i("Places", "An error occurred: " + status);
        }
    };

    public void addAllRequest(ArrayList<Request> requests){

    }

    public void clearRequests(){
        map.clear();
    }

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

//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("GeoView Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
}


// Example of how to remove a marker using this as a hack for onLongClick
//        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                marker.remove();
//            }
//        });

// This method is not needed for loading requests, but demonstrates how to drop a marker
// map.setOnMapLongClickListener(addMarker);

//    // OnLongClickListener for a marker
//    GoogleMap.OnMapLongClickListener addMarker = new GoogleMap.OnMapLongClickListener(){
//        @Override
//        public void onMapLongClick(LatLng pos){
//            if(map != null) {
//                // Move camera to long click position
//                map.moveCamera(CameraUpdateFactory.newLatLng(pos));
//
//                // Drop marker at location
//                map.addMarker(new MarkerOptions().position(pos));
//            }
//        }
//    };