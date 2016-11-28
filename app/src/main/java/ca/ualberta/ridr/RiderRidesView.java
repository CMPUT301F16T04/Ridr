package ca.ualberta.ridr;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the view that shows all of the rides that the Driver is fulfilling. It implements the
 * ACallback interface, which updates code based on our controllers.
 */
public class RiderRidesView extends Activity implements ACallback {
    ListView rideList;
    RideController rides;
    String rider;
    RideAdapter rideAdapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_ride_view);

        rides = new RideController(this, context);
        //retrieve the current driver's UUID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            rider = extras.getString("Name");
        }
        // If no driver is passed in go to login page
        if(rider == null) {
            Intent loginPage = new Intent(RiderRidesView.this, LoginView.class);
            startActivity(loginPage);
            finish();
        }
        rideList = (ListView) findViewById(R.id.driverRidesList);
        rideAdapter = new RideAdapter((Activity) this, new ArrayList<Ride>(), "rider");


        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ride ride = rideAdapter.getItemAtPosition(position);

                Intent intent = new Intent(RiderRidesView.this, RideView.class);
                //based on item add info to intent
                rideAdapter.clear();
                intent.putExtra("username", rider);
                intent.putExtra("rideID", ride.getId().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Get driver rides from the server
        rides = new RideController(this);
        rides.getRiderRides(rider);

        // Set ride adapter
        rideList.setAdapter(rideAdapter);
    }

    @Override
    /**
     * Used when the ride controller is finishes fetching rides from the server or file storage
     */
    public void update() {
        rideAdapter.clear();
        rideAdapter.notifyDataSetChanged();
        rideAdapter.addAll(rides.getAll());
    }

}
