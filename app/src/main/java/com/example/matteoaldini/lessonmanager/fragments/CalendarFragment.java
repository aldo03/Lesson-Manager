package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.activities.DetailsLessonActivity;
import com.example.matteoaldini.lessonmanager.model.ImageUtils;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Filo on 05/05/2015.
 */
public class CalendarFragment extends Fragment {

    public interface CalendarListener{

        List<Lesson> getLessons(Calendar date);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof CalendarListener){
            this.listener = (CalendarListener)activity;
        }
    }

    private CalendarListener listener;
    private View view;
    private MaterialCalendarView calendar;
    private LinearLayout layout;
    private List<Lesson> list;
    private final static int DETAILS_LESSON_CODE = 10;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.calendar_layout, container, false);
        this.calendar = (MaterialCalendarView)this.view.findViewById(R.id.calendarView);
        this.layout = (LinearLayout)this.view.findViewById(R.id.linearLayout);
        this.calendar.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        list = listener.getLessons(calendar.getSelectedDate().getCalendar());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        //super.onPostExecute(aVoid);
                        createLessonCards(inflater);
                    }

                    @Override
                    protected void onPreExecute() {
                        //super.onPreExecute();
                    }
                }.execute();
            }
        });

        return this.view;
    }

    private String getHourFromInt(int time){
        if(time<10){
            return "0"+time;
        }else return ""+time;
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
            tempView = inflater.inflate(R.layout.lesson_layout, null);
            info = (TextView)tempView.findViewById(R.id.student_info);
            startHour = (TextView)tempView.findViewById(R.id.hour_info);
            endHour = (TextView)tempView.findViewById(R.id.hour_info2);
            imgSubject = (ImageView)tempView.findViewById(R.id.imageView);
            imgIconStudent = (ImageView)tempView.findViewById(R.id.userImage);
            card = (CardView)tempView.findViewById(R.id.card_view);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetailsLesson = new Intent(getActivity(), DetailsLessonActivity.class);
                    intentDetailsLesson.putExtra("lesson", lessonTemp);
                    startActivityForResult(intentDetailsLesson, DETAILS_LESSON_CODE);
                }
            });
            info.setText(l.getStudent().getName()+" "+l.getStudent().getSurname());
            startHour.setText(getHourFromInt(l.getHourStart())+":"+getHourFromInt(l.getMinStart()));
            endHour.setText(getHourFromInt(l.getHourEnd()) + ":" + getHourFromInt(l.getMinEnd()));
            ImageUtils.setImageSubject(imgSubject, l.getSubject());
            ImageUtils.setImageFromPosition(imgIconStudent, l.getStudent().getColor());
            layout.addView(tempView);
        }
        layout.invalidate();
    }
}
