package ca.ualberta.ridr;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Created by mackenzie on 13/11/16.
 * http://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
 */
public class RiderRequestListAdapter extends ArrayAdapter<Request> {

    public RiderRequestListAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
    }

    public RiderRequestListAdapter(Context context, int resource, List<Request> requestList){
        super(context, resource, requestList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.rider_request_list_tem, null);
        }

        Request p = getItem(position);

        if(p != null){
            TextView tt1 = (TextView) v.findViewById(R.id.pickup);
            TextView tt2 = (TextView) v.findViewById(R.id.dropoff);
            TextView tt3 = (TextView) v.findViewById(R.id.fare);

            if (tt1 != null) {
                tt1.setText(p.getPickup());
            }

            if (tt2 != null) {
                tt2.setText(p.getDropoff());
            }

            if (tt3 != null) {
                tt3.setText((int) p.getFare());
            }
        }

        return v;
    }
}
