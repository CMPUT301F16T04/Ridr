package ca.ualberta.ridr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Marco on 11-Nov-2016.
 */

public class TimeSelector extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    //fragment that displays a date picker
    // based off of code from http://stackoverflow.com/questions/27225815/android-how-to-show-datepicker-in-fragment

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar cal = Calendar.getInstance();;
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    public void onTimeSet(TimePicker View, int hour, int minute){
        populateSetTime(hour, minute);
    }

    public void populateSetTime(int hour, int minute){
        TextView timeView = (TextView) getActivity().findViewById(R.id.timeText);
        if(hour < 12 ){
            timeView.setText(hour + ":" + minute +" AM");
        } else{
            timeView.setText(hour + ":" + minute + " PM");
        }

    }
}
