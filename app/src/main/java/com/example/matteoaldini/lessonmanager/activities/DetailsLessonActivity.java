package com.example.matteoaldini.lessonmanager.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
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
        this.initFields();

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
        }else if(item.getItemId()==R.id.delete_item_menu){
            new AlertDialog.Builder(this)
                    .setTitle("Delete lesson")
                    .setMessage("Are you sure you want to delete this lesson?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
                            db.deleteLesson(lesson.getId());
                            Intent resIntent = new Intent();
                            setResult(RESULT_OK, resIntent);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            if(requestCode==EDIT_LESSON_CODE){
                if(resultCode==RESULT_OK){
                    this.lesson = (Lesson)data.getSerializableExtra("lesson");
                    this.initFields();
                    Toast.makeText(getApplicationContext(), R.string.lesson_edited, Toast.LENGTH_LONG).show();
                }else if(resultCode==RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), R.string.lesson_not_edited, Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initFields(){
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
}
