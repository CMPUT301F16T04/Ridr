package ca.ualberta.ridr;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Justin Barclay on 20/11/16.
 */
public class RideAdapter extends ArrayAdapter<Ride> {
    private Context context;
    private ArrayList<Ride> rides = new ArrayList<>();


    public RideAdapter(Context context, ArrayList<Ride> rides) {
        super(context, 0, rides);
        this.context = context;
        this.rides = rides;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ride_fragment, parent, false);
        }
        TextView rideCompleted = (TextView) convertView.findViewById(R.id.rideCompleted);
        TextView ridePickup = (TextView) convertView.findViewById(R.id.ridePickup);
        TextView rideDropoff = (TextView) convertView.findViewById(R.id.rideDropoff);
        TextView riderName = (TextView) convertView.findViewById(R.id.riderName);
        TextView ridePickupTime = (TextView) convertView.findViewById(R.id.ridePickupTime);
        TextView rideFare = (TextView) convertView.findViewById(R.id.rideFare);

        Ride ride = getItem(position);

        SimpleDateFormat rideDate = new SimpleDateFormat("HH:mm 'on' dd MMM yyyy");

        String fareText = ride.isPaid()? "Fare(Paid): ": "Fair: ";
        String completed = ride.isCompleted().toString();
        rideCompleted.setText("Completed: " + completed.substring(0, 1).toUpperCase() + completed.substring(1));
        riderName.setText("Rider: " + "John Doe");
        ridePickup.setText("Pickup: " + ride.getPickupAddress());
        rideDropoff.setText("Drop off: " + ride.getDropOffAddress());
        ridePickupTime.setText("Pick up: " + rideDate.format(ride.getRideDate()));
        rideFare.setText(fareText + Float.toString(ride.getFare()));

        if(ride.isCompleted() && ride.isPaid()) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.paid));
        } else if(ride.isCompleted() && !ride.isPaid()){
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.completed));
        } else{
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.uncompleted));
        }

        return convertView;
    }

}




