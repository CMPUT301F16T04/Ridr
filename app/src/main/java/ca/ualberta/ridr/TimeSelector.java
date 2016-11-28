package ca.ualberta.ridr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * fragment that allows the user to chose a time from a clock
 * sets the text in a text view to a time of hh:mm am/pm
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
        String date;
        TextView timeView = (TextView) getActivity().findViewById(R.id.timeText);
        if(hour < 12 && hour > 0){
            date = DateUtils.formatElapsedTime(((hour)*60*60) + (minute*60));
            timeView.setText(date.substring(0, date.length()-3) + " AM");
        } else if(hour == 12){
            // 12 pm
            date = DateUtils.formatElapsedTime(12*60*60 + (minute*60));
            timeView.setText(date.substring(0, date.length()-3) + " PM");
        } else if(hour == 0||hour == 24){
            // 12 am
            date = DateUtils.formatElapsedTime(12*60*60 + (minute*60));
            timeView.setText(date.substring(0, date.length()-3) + " AM");
        } else{
            date = DateUtils.formatElapsedTime(((hour-12)*60*60) + (minute*60));
            timeView.setText(date.substring(0, date.length()-3) + " PM");
        }

    }
}
