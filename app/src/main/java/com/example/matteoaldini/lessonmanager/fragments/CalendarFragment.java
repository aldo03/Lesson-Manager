package com.example.matteoaldini.lessonmanager.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

/**
 * Created by Filo on 05/05/2015.
 */
public class CalendarFragment extends Fragment {

    public interface CalendarListener{

        public List<Lesson> getLessons(CalendarDay date);

    }

    private CalendarListener listener;
    private View view;
    private MaterialCalendarView calendar;
    private LinearLayout layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.calendar_layout, container, false);
        this.calendar = (MaterialCalendarView)this.view.findViewById(R.id.calendarView);
        this.layout = (LinearLayout)this.view.findViewById(R.id.linearLayout);
        this.layout.addView(inflater.inflate(R.layout.lesson_layout, null));
        return this.view;
    }
}
