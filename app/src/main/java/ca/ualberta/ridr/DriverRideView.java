package ca.ualberta.ridr;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DriverRideView extends Activity implements ACallback {
    ListView rideList;
    RideController rides;
    UUID driver;
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
            String driverID = extras.getString("UUID");
            driver = UUID.fromString(driverID);
        }
        rideList = (ListView) findViewById(R.id.driverRidesList);
        rideAdapter = new RideAdapter((Activity) this, new ArrayList<Ride>());
    }

    @Override
    protected void onStart(){
        super.onStart();
        rides.getDriverRides(driver);

        rideList.setAdapter(rideAdapter);
    }

    @Override
    public void update() {
        rideAdapter.clear();
        rideAdapter.addAll(rides.getAll());
    }

}
