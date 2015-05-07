package com.example.matteoaldini.lessonmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;

import java.util.Calendar;

/**
 * Created by Filo on 07/05/2015.
 */
public class AddLessonActivity extends ActionBarActivity implements DatePickerFragment.DatePickerObserver, TimePickerFragment.TimePickerObserver {
    private static final int DATE_DIALOG_ID = 999;
    private Toolbar toolbar;
    private Student student;
    private TextView date;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private EditText fare;
    private EditText location;
    private Calendar calendar;
    private Spinner spinner;
    private CheckBox checkBox;
    private RelativeLayout rLayout;
    private DatePickerFragment datePicker;
    private DatePickerFragment endDatePicker;
    private TimePickerFragment startTimePicker;
    private TimePickerFragment endTimePicker;
    private Button addLessonBut;
    private int year;
    private int month;
    private int day;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lesson_layout);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);

        Intent i = getIntent();
        this.student = (Student)i.getSerializableExtra("student");

        this.date = (TextView) findViewById(R.id.dateLesson);
        this.endDate = (TextView) findViewById(R.id.endDateLesson);
        this.startTime = (TextView) findViewById(R.id.startLesson);
        this.startTime.setText("00:00");
        this.endTime = (TextView) findViewById(R.id.endLesson);
        this.endTime.setText("00:00");
        this.fare = (EditText) findViewById(R.id.fareLesson);
        this.location = (EditText) findViewById(R.id.locationLesson);
        this.calendar = Calendar.getInstance();
        this.year = this.calendar.get(Calendar.YEAR);
        this.month = this.calendar.get(Calendar.MONTH);
        this.day = this.calendar.get(Calendar.DAY_OF_MONTH);
        this.endYear = this.calendar.get(Calendar.YEAR);
        this.endMonth = this.calendar.get(Calendar.MONTH);
        this.endDay = this.calendar.get(Calendar.DAY_OF_MONTH);
        this.startHour = 0;
        this.startMin = 0;
        this.endHour = 0;
        this.endMin = 0;
        this.date.setText(""+this.day+" / "+this.month+" / "+this.year);
        this.endDate.setText("" + this.endDay + " / " + this.endMonth + " / " + this.endYear);
        this.datePicker = new DatePickerFragment();
        this.endDatePicker = new DatePickerFragment();
        this.startTimePicker = new TimePickerFragment();
        this.endTimePicker = new TimePickerFragment();
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "datePicker");
                datePicker.setStart(true);
            }
        });
        this.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePicker.show(getSupportFragmentManager(), "datePicker");
                endDatePicker.setStart(false);
            }
        });
        this.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker.show(getSupportFragmentManager(), "timePicker");
                startTimePicker.setStart(true);
            }
        });
        this.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker.show(getSupportFragmentManager(), "timePicker");
                endTimePicker.setStart(false);
            }
        });
        this.spinner = (Spinner) findViewById(R.id.frequencyspinner);
        this.rLayout = (RelativeLayout) findViewById(R.id.layoutFrequency);
        this.checkBox = (CheckBox) findViewById(R.id.checkFrequency);
        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rLayout.setVisibility(View.VISIBLE);
                } else{
                    rLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        this.addLessonBut = (Button)findViewById(R.id.addLessonBut);
        this.addLessonBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addNewLesson();
            }
        });
    }

    @Override
    public void dateChanged(int year, int month, int day, boolean startEnd) {
        if(startEnd) {
            this.year = year;
            this.month = month;
            this.day = day;

            this.date.setText("" + this.day + " / " + this.month + " / " + this.year);
        }else {
            this.endYear = year;
            this.endMonth = month;
            this.endDay = day;

            this.endDate.setText("" + this.endDay + " / " + this.endMonth + " / " + this.endYear);
        }
    }

    @Override
    public void timeChanged(int hour, int minute, boolean startEnd) {
        if(startEnd){
            this.startHour = hour;
            this.startMin = minute;
            this.setStartTime(this.startHour, this.startMin);
        }else {
            this.endHour = hour;
            this.endMin = minute;
            this.setEndTime(this.endHour, this.endMin);
        }
    }

    private void setStartTime(int hour, int minute){
        String h="";
        String m="";
        if(hour<10){
            h="0";
        }
        if(minute<10){
            m="0";
        }

        this.startTime.setText(h+hour+":"+m+minute);
    }

    private void setEndTime(int hour, int minute){
        String h="";
        String m="";
        if(hour<10){
            h="0";
        }
        if(minute<10){
            m="0";
        }

        this.endTime.setText(h+hour+":"+m+minute);
    }

    private void addNewLesson(){
        Lesson lesson;
        Calendar day = Calendar.getInstance();
        day.set(this.year, this.month, this.day);
        Calendar endDay = Calendar.getInstance();
        endDay.set(this.endYear, this.endMonth, this.endDay);
        lesson = new Lesson(this.student, day, this.startHour, this.startMin, this.endHour, this.endMin,
                Integer.parseInt(this.fare.getText().toString()), this.location.getText().toString());
        LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
        if(this.checkBox.isChecked()){
            db.insertNewLesson(lesson, this.spinner.getSelectedItemPosition() + 1,endDay);
        }else {
            db.insertNewLesson(lesson, 0 ,endDay);
        }
    }
}
