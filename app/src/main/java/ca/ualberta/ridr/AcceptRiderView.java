package ca.ualberta.ridr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AcceptRiderView extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView requestFrom;
    private TextView payment;
    private TextView pickupTime;
    private TextView startLocation;
    private TextView endLocation;
    private TextView status;
    private Button acceptRider;
    private Button viewRequestInfo;

    private String username;
    private UUID requestID;
    private Rider requestRider;
    private Request request;

    private boolean agreedToFulfill = false;

    //gmap variables
    private GoogleMap gMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng lastKnownPlace;
    private boolean firstLoad;
    private Marker startMarker;
    private Marker endMarker;
    private Geocoder geocoder;

    private RequestController requestController = new RequestController();
    private RiderController riderController = new RiderController();
    private DriverController driverController = new DriverController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_rider);

        //set google map stuff
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.requestMap);
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
        pickupTime = (TextView) findViewById(R.id.pickup_time_accept_rider);
        startLocation = (TextView) findViewById(R.id.start_location);
        endLocation = (TextView) findViewById(R.id.end_location);
        status = (TextView) findViewById(R.id.status_accept_rider);
        viewRequestInfo = (Button) findViewById(R.id.view_request_info_button);
        acceptRider = (Button) findViewById(R.id.accept_rider_button);

        username = "joe";
        requestID = UUID.fromString("23d361d8-02b9-4dda-ae15-71f21e14e908");
//
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if(extras!=null)
//        {
//            username = extras.getString("username");
//            requestID = UUID.fromString(extras.getString("RequestUUID"));
//        } else {
//            Log.i("Intent Extras Error", "Error getting driver and request ID from extras in AcceptRiderView");
//            Intent intent = new Intent(AcceptRiderView.this, LoginView.class);
//            startActivity(intent);
//        }

        //TODO dont really need to get rider once we fix the names thing
        request = requestController.getRequestFromServer(requestID.toString());
        requestRider = riderController.getRiderFromServer(request.getRider());

        String isFrom = "Request from " + capitalizeName(requestRider.getName()) + ":";

        SimpleDateFormat rideDate = new SimpleDateFormat("HH:mm 'on' dd MMM yyyy");


        //set all the text that needs to be set
        requestFrom.setText(isFrom);
        String paymentText = payment.getText() + space+ Float.toString(request.getFare());
        payment.setText(paymentText);
        String pickupTimeText = pickupTime.getText() + space + rideDate.format(request.getDate());
        pickupTime.setText(pickupTimeText);
        String startLocText = startLocation.getText() + space + request.getPickup();
        startLocation.setText(startLocText);
        String endLocText = endLocation.getText() + space + request.getDropoff();
        endLocation.setText(endLocText);

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
                intent.putExtra("username", requestRider.getName());
                startActivity(intent);
            }
        });

        viewRequestInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                slideUp();
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
                    //For now!!!
                    //requestController.addDriverToList(request, username);

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

        Calendar current = Calendar.getInstance();

        // Add night view for nice viewing when it's dark out
        // Dark styling is easier on the eyes
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        if (nightTime(time.format(current.getTime()))) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_night_style);
            gMap.setMapStyle(style);
        }

        setupMap(request);

    }

    /**
     *  sets up the map by adding the two markers and then zooms
     *  zoom is delegated to zoomToMid function
     *
     * @param request used to set marker points
     */
    //TODO put the route between start and end on the map
    private void setupMap(final Request request){

        //adds markers and then move camera to where they are
        startMarker = gMap.addMarker(new MarkerOptions().position(request.getPickupCoords()).title("Pickup: " + request.getPickup()));
        //startMarker.setTag(0);
        endMarker = gMap.addMarker(new MarkerOptions().position(request.getDropOffCoords()).title("Drop off: " + request.getDropoff()));
        //endMarker.setTag(0);

        GoogleMap.OnMarkerClickListener markerClick = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        };

        gMap.setOnMarkerClickListener(markerClick);
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
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 400));
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
            if (drivers.get(i).equals(username)) {
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

    //    http://stackoverflow.com/questions/31394829/how-to-disable-button-while-alphaanimation-running
// How to disable a button during animations
    private void slideUp(){
        LinearLayout hiddenPanel = (LinearLayout)findViewById(R.id.requestInfo);
        if(hiddenPanel.getVisibility() == View.VISIBLE){
            Animation topDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_to_bottom);
            topDown.setAnimationListener(listener);
            hiddenPanel.startAnimation(topDown);
            hiddenPanel.setVisibility(View.INVISIBLE);

        } else {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_from_bottom);
            bottomUp.setAnimationListener(listener);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            //this will allow the displaly to be clickable and if clicked will hide it
            //necessary in case the displayed window covers the markers
            hiddenPanel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    slideUp();
                }
            });

        }
    }

    Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

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

}



