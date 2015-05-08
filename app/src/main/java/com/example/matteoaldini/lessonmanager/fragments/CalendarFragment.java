package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
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

        public List<Lesson> getLessons(Calendar date);

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

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.calendar_layout, container, false);
        this.calendar = (MaterialCalendarView)this.view.findViewById(R.id.calendarView);
        this.layout = (LinearLayout)this.view.findViewById(R.id.linearLayout);
        this.calendar.setOnDateChangedListener(new OnDateChangedListener() {
            @Override
            public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                layout.removeAllViews();
                View tempView;
                TextView info;
                TextView startHour;
                TextView endHour;
                CardView card;
                ImageView img;
                Lesson l;
                List<Lesson> list = listener.getLessons(calendar.getSelectedDate().getCalendar());
                Iterator<Lesson> iterator = list.iterator();
                while(iterator.hasNext()){
                    tempView = inflater.inflate(R.layout.lesson_layout, null);
                    info = (TextView)tempView.findViewById(R.id.student_info);
                    startHour = (TextView)tempView.findViewById(R.id.hour_info);
                    endHour = (TextView)tempView.findViewById(R.id.hour_info2);
                    img = (ImageView)tempView.findViewById(R.id.imageView);
                    card = (CardView)tempView.findViewById(R.id.card_view);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("","asdasd");
                        }
                    });
                    l = iterator.next();
                    info.setText(l.getStudent().getName()+" "+l.getStudent().getSurname());
                    startHour.setText(getHourFromInt(l.getHourStart())+":"+getHourFromInt(l.getMinStart()));
                    endHour.setText(getHourFromInt(l.getHourEnd()) + ":" + getHourFromInt(l.getMinEnd()));
                    setImage(img, l.getSubject());
                    layout.addView(tempView);
                }
                layout.invalidate();
            }
        });

        return this.view;
    }

    private String getHourFromInt(int time){
        if(time<10){
            return "0"+time;
        }else return ""+time;
    }

    private void setImage(ImageView img, String s){
        switch (s){
            case "Biology":
                img.setImageResource(R.drawable.biology);
                break;
            case "Chemistry":
                img.setImageResource(R.drawable.chemistry);
                break;
            case "Computer Science":
                img.setImageResource(R.drawable.computerscience);
                break;
            case "Economy":
                img.setImageResource(R.drawable.economy);
                break;
            case "English":
                img.setImageResource(R.drawable.english);
                break;
            case "Geography":
                img.setImageResource(R.drawable.geography);
                break;
            case "Greek":
                img.setImageResource(R.drawable.greek);
                break;
            case "History":
                img.setImageResource(R.drawable.history);
                break;
            case "Italian":
                img.setImageResource(R.drawable.italian);
                break;
            case "Languages":
                img.setImageResource(R.drawable.language);
                break;
            case "Latin":
                img.setImageResource(R.drawable.latin);
                break;
            case "Law":
                img.setImageResource(R.drawable.law);
                break;
            case "Maths":
                img.setImageResource(R.drawable.maths);
                break;
            case "Music":
                img.setImageResource(R.drawable.music);
                break;
            case "Physics":
                img.setImageResource(R.drawable.physics);
                break;
            case "Science":
                img.setImageResource(R.drawable.science);
                break;
        }
    }
}
