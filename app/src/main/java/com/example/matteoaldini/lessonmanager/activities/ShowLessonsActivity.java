package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;


import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.fragments.CardFragment;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.utils.ImageUtils;
import com.example.matteoaldini.lessonmanager.utils.TimeUtils;

import java.text.ParseException;
import java.util.List;

/**
 * Created by brando on 14/06/2015.
 */
public class ShowLessonsActivity extends ActionBarActivity {
    private CardFragment cardFragment;
    private LinearLayout layout;
    private List<Lesson> list;
    private Student student;
    private View view;
    private Toolbar toolbar;
    private static final int DETAILS_LESSON_CODE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        this.student = (Student) intent.getSerializableExtra("student");
        setContentView(R.layout.show_lessons_layout);
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.setSupportActionBar(toolbar);
        this.toolbar.setTitle("'s lessons");
        this.layout = (LinearLayout) findViewById(R.id.layoutLinear);
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
                try {
                    list = db.getStudentLessons(student.getId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                createLessonCards(getLayoutInflater());
            }



        }.execute();

    }

    private void createLessonCards(LayoutInflater inflater){
        layout.removeAllViews();
        View tempView;
        TextView info;
        TextView startHour;
        TextView endHour;
        CardView card;
        ImageView imgSubject;
        ImageView imgIconStudent;
        for(Lesson l : this.list){
            final Lesson lessonTemp = l;
            tempView = getLayoutInflater().inflate(R.layout.lesson_layout, null);
            info = (TextView)tempView.findViewById(R.id.student_info);
            startHour = (TextView)tempView.findViewById(R.id.hour_info);
            endHour = (TextView)tempView.findViewById(R.id.hour_info2);
            imgSubject = (ImageView)tempView.findViewById(R.id.imageView);
            imgIconStudent = (ImageView)tempView.findViewById(R.id.userImage);
            card = (CardView)tempView.findViewById(R.id.card_view);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetailsLesson = new Intent(getApplicationContext(), DetailsLessonActivity.class);
                    intentDetailsLesson.putExtra("lesson", lessonTemp);
                    startActivityForResult(intentDetailsLesson, DETAILS_LESSON_CODE);
                }
            });
            info.setText(l.getStudent().getName()+" "+l.getStudent().getSurname());
            TimeUtils.setTime(l.getHourStart(), l.getMinStart(), startHour);
            TimeUtils.setTime(l.getHourEnd(), l.getMinEnd(), endHour);
            ImageUtils.setImageSubject(imgSubject, l.getSubject());
            ImageUtils.setImageFromPosition(imgIconStudent, l.getStudent().getColor());
            layout.addView(tempView);
        }
        layout.invalidate();
    }
}
