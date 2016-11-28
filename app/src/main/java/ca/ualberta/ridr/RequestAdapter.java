package ca.ualberta.ridr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jferris on 13/11/16.
 *
 * Given a Request object, sets Textview elements in the listview that is calling this adapter,
 * using the information from the request.
 */
public class RequestAdapter extends ArrayAdapter<Request> {
    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();
    private String userName;


    public RequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.context = context;
        this.requests = requests;
    }

    public RequestAdapter(Context context, ArrayList<Request> requests, String userName) {
        super(context, 0, requests);
        this.context = context;
        this.requests = requests;
        this.userName = userName;
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

        String placeholder;
        placeholder = "Rider: " + request.getRider();
        requestUser.setText(placeholder);
        placeholder = "Pickup: " + request.getPickup();
        requestPickup.setText(placeholder);
        placeholder = "Drop off: " + request.getDropoff();
        requestDropoff.setText(placeholder);
        placeholder = "Date: " + request.getDate().toString();
        requestDate.setText(placeholder);
        placeholder = "Fare: " + Float.toString(request.getFare());
        requestFare.setText(placeholder);
        placeholder = "Status: " + getRequestStatusString(request);
        requestStatus.setText(placeholder);

        return convertView;
    }



    private String getRequestStatusString(Request request){
        if(request.isAccepted()){
            return "Has Driver"; //Really should never actually be displaying this, as this means
            //that the object is now a ride, so why are we displaying with the request adapter...?
        } else if (request.getPossibleDrivers() == null || request.getPossibleDrivers().size() == 0){
            return "No Drivers willing to fulfill yet";
        } else if(request.getPossibleDrivers().size() > 0) {
            if(userName != null && request.getPossibleDrivers().contains(userName)){
                return "You are willing to fulfill"; //only happens if calling as a driver, from DriverRequestsView
            }
            return "A few Drivers willing to fulfill";
        } else {
            return "";
        }
    }
}



