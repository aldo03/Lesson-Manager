package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.model.ImageUtils;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.model.TimeUtils;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;

/**
 * Created by brando on 02/06/2015.
 */
public class DetailsLessonActivity extends ActionBarActivity  {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String LESSON_EXTRA = "lesson";
    private static final int EDIT_LESSON_CODE = 5;
    private Toolbar toolbar;
    private Lesson lesson;
    private ImageView image;
    private TextView name;
    private TextView startTime;
    private TextView endTime;
    private TextView date;
    private TextView location;
    private TextView fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.lesson = (Lesson) intent.getSerializableExtra("lesson");
        setContentView(R.layout.details_lesson_layout);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setBackgroundColor(ImageUtils.getDarkColor(this.lesson.getStudent().getColor(), getApplicationContext()));
        setSupportActionBar(this.toolbar);
        this.name = (TextView) findViewById(R.id.name);
        this.startTime = (TextView) findViewById(R.id.startTime);
        this.endTime = (TextView) findViewById(R.id.endTime);
        this.date = (TextView) findViewById(R.id.date);
        this.location = (TextView) findViewById(R.id.address);
        this.fare = (TextView) findViewById(R.id.fare);
        this.name.setText(this.lesson.getStudent().getName() +" "+ lesson.getStudent().getSurname());
        TimeUtils.setTime(this.lesson.getHourStart(), this.lesson.getMinStart(), this.startTime);
        TimeUtils.setTime(this.lesson.getHourEnd(), this.lesson.getMinEnd(), this.endTime);
        this.date.setText(new SimpleDateFormat(DATE_FORMAT).format(this.lesson.getDate().getTime()));
        this.fare.setText(""+this.lesson.getFare());
        this.location.setText(this.lesson.getLocation());
        this.image = (ImageView) findViewById(R.id.lessonImage);
        ImageUtils.setImageSubject(this.image, this.lesson.getSubject());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_student,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit_item_menu){
            Intent intentEditStudent = new Intent(getApplicationContext(), EditLessonActivity.class);
            intentEditStudent.putExtra(LESSON_EXTRA, this.lesson);
            startActivityForResult(intentEditStudent, EDIT_LESSON_CODE);
        }
        return super.onOptionsItemSelected(item);
    }
}
