package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.fragments.DatePickerFragment;
import com.example.matteoaldini.lessonmanager.fragments.TimePickerFragment;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.model.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by brando on 04/06/2015.
 */
public class EditLessonActivity extends ActionBarActivity implements DatePickerFragment.DatePickerObserver, TimePickerFragment.TimePickerObserver {
    private static final String LESSON_EXTRA = "lesson";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private Toolbar toolbar;
    private Lesson lesson;
    private TextView date;
    private TextView startTime;
    private TextView endTime;
    private EditText fare;
    private EditText location;
    private Calendar calendar;
    private Spinner subjects_spinner;
    private DatePickerFragment datePicker;
    private DatePickerFragment endDatePicker;
    private TimePickerFragment startTimePicker;
    private TimePickerFragment endTimePicker;
    private CheckBox paid;
    private CheckBox presence;
    private int year;
    private int month;
    private int day;
    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_lesson_layout);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);

        Intent i = getIntent();
        this.lesson = (Lesson)i.getSerializableExtra(LESSON_EXTRA);

        this.date = (TextView) findViewById(R.id.dateLesson);
        this.startTime = (TextView) findViewById(R.id.startLesson);
        this.startTime.setText("00:00");
        this.endTime = (TextView) findViewById(R.id.endLesson);
        this.endTime.setText("00:00");
        this.fare = (EditText) findViewById(R.id.fareLesson);
        this.location = (EditText) findViewById(R.id.locationLesson);
        this.paid = (CheckBox) findViewById(R.id.paidCheckbox);
        this.presence = (CheckBox) findViewById(R.id.presenceCheckbox);
        String[] s = (new SimpleDateFormat(DATE_FORMAT).format(this.lesson.getDate().getTime()).split("-"));
        this.year = Integer.parseInt(s[2]);
        this.month = Integer.parseInt(s[1]);
        this.day = Integer.parseInt(s[0]);
        this.startHour = this.lesson.getHourStart();
        this.startMin = this.lesson.getMinStart();
        this.endHour = this.lesson.getHourEnd();
        this.endMin = this.lesson.getMinEnd();
        this.location.setText(this.lesson.getLocation());
        this.fare.setText(""+this.lesson.getFare());
        this.date.setText(""+this.day+" / "+(this.month+1)+" / "+this.year);
        this.datePicker = new DatePickerFragment();
        this.endDatePicker = new DatePickerFragment();
        this.startTimePicker = new TimePickerFragment();
        this.endTimePicker = new TimePickerFragment();
        this.paid.setChecked(this.lesson.isPaid());
        this.presence.setChecked(this.lesson.isPresent());
        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(), "datePicker");
                datePicker.setStart(true);
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
        this.subjects_spinner = (Spinner) findViewById(R.id.subject_spinner);
    }

    @Override
    public void dateChanged(int year, int month, int day, boolean startEnd) {
        if(startEnd) {
            this.year = year;
            this.month = month;
            this.day = day;

            this.date.setText("" + this.day + " / " + (this.month+1) + " / " + this.year);
        }
    }

    @Override
    public void timeChanged(int hour, int minute, boolean startEnd) {
        if(startEnd){
            this.startHour = hour;
            this.startMin = minute;
            TimeUtils.setTime(this.startHour, this.startMin, this.startTime);
        }else {
            this.endHour = hour;
            this.endMin = minute;
            TimeUtils.setTime(this.endHour, this.endMin, this.endTime);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_or_edit_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_or_edit_item_menu){
            if(this.location.getText().toString().equals("")||this.fare.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), R.string.field_missing, Toast.LENGTH_LONG).show();
            }else if((this.startHour*60+this.startMin)>=(this.endHour*60+this.endMin)){
                Toast.makeText(getApplicationContext(), R.string.start_end_hour_wrong, Toast.LENGTH_LONG).show();
            }else {
                Calendar date = Calendar.getInstance();
                date.set(this.year, this.month, this.day);
                LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
                Lesson modifiedLesson = new Lesson(this.lesson.getStudent(), date, this.startHour, this.startMin, this.endHour, this.endMin,
                        Integer.parseInt(this.fare.getText().toString()), this.location.getText().toString(), this.subjects_spinner.getSelectedItem().toString());
                modifiedLesson.setIdLesson(this.lesson.getId());
                Lesson conflictLesson = db.updateLesson(modifiedLesson);
                if (conflictLesson != null) {
                    Toast.makeText(getApplicationContext(), R.string.lesson_not_edited, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.lesson_edited, Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
