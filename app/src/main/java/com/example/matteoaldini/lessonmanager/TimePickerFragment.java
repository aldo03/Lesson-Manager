package com.example.matteoaldini.lessonmanager;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Filo on 07/05/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerObserver{
        public void timeChanged(int hour, int minute, boolean startEnd);
    }

    private TimePickerObserver listener;
    private boolean startEnd;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof TimePickerObserver){
            this.listener = (TimePickerObserver) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, 0, 0,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.listener.timeChanged(hourOfDay, minute, this.startEnd);
    }

    public void setStart(boolean startEnd){  //if true -> start, else end
        this.startEnd = startEnd;
    }
}