package ca.ualberta.ridr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jferris on 13/11/16.
 */
public class RequestAdapter extends ArrayAdapter<Request> {
    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();


    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.context = context;
        this.requests = requests;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item, parent, false);
        }
        TextView requestPickup = (TextView) convertView.findViewById(R.id.request_pickup);
        TextView requestDropoff = (TextView) convertView.findViewById(R.id.request_dropoff);
        TextView requestDate = (TextView) convertView.findViewById(R.id.request_date);
        TextView requestUser = (TextView) convertView.findViewById(R.id.request_user);
        TextView requestFare = (TextView) convertView.findViewById(R.id.request_fare);
        TextView requestStatus = (TextView) convertView.findViewById(R.id.request_status);


        Request request = getItem(position);

        requestUser.setText("Rider: " + request.getRider());
        requestPickup.setText("Pickup: " + request.getPickup());
        requestDropoff.setText("Drop off: " + request.getDropoff());
        requestDate.setText("Date: " + request.getDate().toString());
        requestFare.setText("Fare: " + Float.toString(request.getFare()));

        requestStatus.setText("Status: " + getRequestStatus(request));



        return convertView;
    }



    private String getRequestStatus(Request request){
        if(request.isAccepted()){
            return "Has Driver"; //Really should never actually be displaying this, as this means
            //that the object is now a ride, so why are we displaying with the request adapter...?
        } else if (request.getPossibleDrivers() == null || request.getPossibleDrivers().size() == 0){
            return "No Drivers willing to fulfill yet";
        } else if(request.getPossibleDrivers().size() > 0) {
            return "A few Drivers willing to fulfill";
        } else {
            return "";
        }
    }
}



