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

import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.fragments.DatePickerFragment;
import com.example.matteoaldini.lessonmanager.fragments.TimePickerFragment;
import com.example.matteoaldini.lessonmanager.model.TimeUtils;

import java.util.Calendar;

/**
 * Created by Filo on 07/05/2015.
 */
public class AddLessonActivity extends ActionBarActivity implements DatePickerFragment.DatePickerObserver, TimePickerFragment.TimePickerObserver {
    private static final String STUDENT_EXTRA = "student";
    private Toolbar toolbar;
    private Student student;
    private TextView date;
    private TextView endDate;
    private TextView startTime;
    private TextView endTime;
    private EditText fare;
    private EditText location;
    private Calendar calendar;
    private Spinner frequency_spinner;
    private Spinner subjects_spinner;
    private CheckBox checkBox;
    private RelativeLayout rLayout;
    private DatePickerFragment datePicker;
    private DatePickerFragment endDatePicker;
    private TimePickerFragment startTimePicker;
    private TimePickerFragment endTimePicker;
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
        this.student = (Student)i.getSerializableExtra(STUDENT_EXTRA);

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
        this.date.setText(""+this.day+" / "+(this.month+1)+" / "+this.year);
        this.endDate.setText("" + this.endDay + " / " + (this.endMonth+1) + " / " + this.endYear);
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
        this.frequency_spinner = (Spinner) findViewById(R.id.frequencyspinner);
        this.subjects_spinner = (Spinner) findViewById(R.id.subject_spinner);
        this.rLayout = (RelativeLayout) findViewById(R.id.layoutFrequency);
        this.checkBox = (CheckBox) findViewById(R.id.checkFrequency);
        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rLayout.setVisibility(View.VISIBLE);
                } else {
                    rLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void dateChanged(int year, int month, int day, boolean startEnd) {
        if(startEnd) {
            this.year = year;
            this.month = month;
            this.day = day;

            this.date.setText("" + this.day + " / " + (this.month+1) + " / " + this.year);
        }else {
            this.endYear = year;
            this.endMonth = month;
            this.endDay = day;

            this.endDate.setText("" + this.endDay + " / " + (this.endMonth+1) + " / " + this.endYear);
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

    private void addNewLesson(){
        Lesson lesson;
        Calendar day = Calendar.getInstance();
        day.set(this.year, this.month, this.day);
        Calendar endDay = Calendar.getInstance();
        endDay.set(this.endYear, this.endMonth, this.endDay);

        LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
        if(this.location.getText().toString().equals("")||this.fare.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), R.string.field_missing, Toast.LENGTH_LONG).show();
        }else if((this.startHour*60+this.startMin)>=(this.endHour*60+this.endMin)){
            Toast.makeText(getApplicationContext(), R.string.start_end_hour_wrong, Toast.LENGTH_LONG).show();
        }else {
            lesson = new Lesson(this.student, day, this.startHour, this.startMin, this.endHour, this.endMin,
                    Integer.parseInt(this.fare.getText().toString()), this.location.getText().toString(),this.subjects_spinner.getSelectedItem().toString());
            if(this.checkBox.isChecked()){
                if(day.compareTo(endDay)>=0){
                    Toast.makeText(getApplicationContext(), R.string.wrong_end_date, Toast.LENGTH_LONG).show();
                }else {
                    db.insertNewLesson(lesson, this.frequency_spinner.getSelectedItemPosition() + 1,endDay);
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }else {
                db.insertNewLesson(lesson, 0 ,endDay);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_item_menu){
            this.addNewLesson();
        }
        return super.onOptionsItemSelected(item);
    }
}
