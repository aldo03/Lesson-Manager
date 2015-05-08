package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
                View tempView;
                TextView info;
                TextView startHour;
                TextView endHour;
                Lesson l;
                List<Lesson> list = listener.getLessons(calendar.getSelectedDate().getCalendar());
                Iterator<Lesson> iterator = list.iterator();
                while(iterator.hasNext()){
                    tempView = inflater.inflate(R.layout.lesson_layout, null);
                    info = (TextView)tempView.findViewById(R.id.student_info);
                    startHour = (TextView)tempView.findViewById(R.id.hour_info);
                    endHour = (TextView)tempView.findViewById(R.id.hour_info2);
                    l = iterator.next();
                    info.setText(l.getStudent().getName()+" "+l.getStudent().getSurname());
                    startHour.setText(l.getHourStart()+":"+l.getMinStart());
                    endHour.setText(l.getHourEnd()+":"+l.getMinEnd());
                    layout.addView(tempView);
                }
            }
        });

        return this.view;
    }


}
