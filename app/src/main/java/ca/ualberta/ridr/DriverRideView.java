package ca.ualberta.ridr;

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
public class DriverRideView extends Activity implements ACallback {
    ListView rideList;
    RideController rides;
    String driver;
    RideAdapter rideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_ride_view);

        rides = new RideController(this);
        //retrieve the current driver's UUID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            driver = extras.getString("Name");
        }
        // If no driver is passed in go to login page
        if(driver == null) {
            Intent loginPage = new Intent(DriverRideView.this, LoginView.class);
            startActivity(loginPage);
            finish();
        }
        rideList = (ListView) findViewById(R.id.driverRidesList);
        rideAdapter = new RideAdapter((Activity) this, new ArrayList<Ride>(), "driver");


        rideList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ride ride = rideAdapter.getItemAtPosition(position);

                Intent intent = new Intent(DriverRideView.this, RideView.class);
                //based on item add info to intent
                intent.putExtra("username", driver);
                intent.putExtra("rideID", ride.getId().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Get driver rides from the server
        rides.getDriverRides(driver);

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
