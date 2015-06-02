package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
    private Toolbar toolbar;
    private Lesson lesson;
    private ImageView image;
    private TextView name;
    private TextView startTime;
    private TextView endTime;
    private TextView date;
    private TextView location;
    private TextView fare;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.lesson = (Lesson) intent.getSerializableExtra("lesson");
        setContentView(R.layout.details_lesson_layout);
        this.sdf = new SimpleDateFormat(DATE_FORMAT);
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
        this.date.setText(sdf.format(this.lesson.getDate().getTime()));
        this.fare.setText(""+this.lesson.getFare());
        this.location.setText(this.lesson.getLocation());
        this.image = (ImageView) findViewById(R.id.lessonImage);
        ImageUtils.setImageSubject(this.image, this.lesson.getSubject());

    }
}
