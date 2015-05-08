package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Filo on 07/05/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerObserver{
        public void dateChanged(int year, int month, int day, boolean startEnd);
    }

    private DatePickerObserver listener;
    private boolean startEnd;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof DatePickerObserver){
            this.listener = (DatePickerObserver) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.listener.dateChanged(year, month, day, this.startEnd);
    }

    public void setStart(boolean startEnd){
        this.startEnd = startEnd;
    }
}