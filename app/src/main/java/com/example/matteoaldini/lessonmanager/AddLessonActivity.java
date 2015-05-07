package com.example.matteoaldini.lessonmanager;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Filo on 07/05/2015.
 */
public class AddLessonActivity extends ActionBarActivity {
    private static final int DATE_DIALOG_ID = 999;
    private Toolbar toolbar;
    private TextView date;
    private TextView startTime;
    private TextView endTime;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lesson_layout);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);
        this.date = (TextView) findViewById(R.id.dateLesson);
        this.startTime = (TextView) findViewById(R.id.startLesson);
        this.endTime = (TextView) findViewById(R.id.endLesson);
        this.calendar = Calendar.getInstance();
        this.date.setText(""+this.calendar.get(Calendar.DAY_OF_MONTH)+" / "+this.calendar.get(Calendar.MONTH)+" / "+this.calendar.get(Calendar.YEAR));
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }
}
