package ca.ualberta.ridr;

import android.content.Context;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_fragment, parent, false);
        }
        TextView ridePickup = (TextView) convertView.findViewById(R.id.ridePickup);
        TextView rideDropoff = (TextView) convertView.findViewById(R.id.rideDropoff);
        TextView riderName = (TextView) convertView.findViewById(R.id.riderName);
        TextView ridePickupTime = (TextView) convertView.findViewById(R.id.ridePickupTime);
        TextView rideFare = (TextView) convertView.findViewById(R.id.rideFare);

        Ride ride = getItem(position);

        SimpleDateFormat rideDate = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss z");

        riderName.setText("Rider: " + ride.getRider());
        ridePickup.setText("Pickup: " + ride.getPickupAddress());
        rideDropoff.setText("Drop off: " + ride.getDropOffAddress());
        ridePickupTime.setText("Pickup Time: " + rideDate.format(ride.getRideDate()));
        rideFare.setText("Fare: " + Float.toString(ride.getFare()));



        return convertView;
    }

}




