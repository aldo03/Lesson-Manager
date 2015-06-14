package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filo on 04/06/2015.
 */
public class CashGestureFragment extends Fragment implements DatePickerFragment.DatePickerObserver{

    @Override
    public void dateChanged(int year, int month, int day, boolean startEnd) {
        if(startEnd) {
            this.yearStart = year;
            this.monthStart = month;
            this.dayStart = day;
            this.dateStart.setText("" + this.dayStart + " / " + (this.monthStart+1) + " / " + this.yearStart);
        }else {
            this.yearEnd = year;
            this.monthEnd = month;
            this.dayEnd = day;
            this.dateEnd.setText("" + this.dayEnd + " / " + (this.monthEnd+1) + " / " + this.yearEnd);
        }
    }

    public interface CashGestureListener {

        void payForSomeone(List<Student> students, List<Lesson> lessons) throws ParseException;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof CashGestureListener){
            this.listener = (CashGestureListener)activity;
        }
    }

    private CashGestureListener listener;
    protected HorizontalBarChart mChart;
    private View view;
    private DatePickerFragment dateStartPicker;
    private DatePickerFragment dateEndPicker;
    private TextView dateStart;
    private TextView dateEnd;
    private int yearStart;
    private int monthStart;
    private int dayStart;
    private int yearEnd;
    private int monthEnd;
    private int dayEnd;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.cash_gesture_layout, container, false);



        this.dateStart = (TextView)this.view.findViewById(R.id.date_start_graphic);
        this.dateEnd = (TextView)this.view.findViewById(R.id.date_end_graphic);
        this.dateStartPicker = new DatePickerFragment();
        this.dateEndPicker = new DatePickerFragment();
        this.dateStartPicker.manuallyAttachObserver(this);
        this.dateEndPicker.manuallyAttachObserver(this);

        this.yearStart = 2010;
        this.monthStart = 1;
        this.dayStart = 1;

        this.yearEnd = 2016;
        this.monthEnd = 1;
        this.dayEnd = 1;

        this.dateStart.setText(""+this.dayStart+" / "+this.monthStart+" / "+this.yearStart);
        this.dateEnd.setText(""+this.dayEnd+" / "+this.monthEnd+" / "+this.yearEnd);

        this.dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStartPicker.show(getFragmentManager(), "datePicker");
                dateStartPicker.setStart(true);
            }
        });

        this.dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEndPicker.show(getFragmentManager(), "datePicker");
                dateEndPicker.setStart(false);
            }
        });



        this.mChart = (HorizontalBarChart)this.view.findViewById(R.id.chart1);
        this.mChart.setDrawBarShadow(false);

        this.mChart.setDrawValueAboveBar(true);

        this.mChart.setDescription("");
        this.mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        this.mChart.setPinchZoom(false);


        this.mChart.setDrawGridBackground(false);
        XAxis xl = this.mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = this.mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        YAxis yr = this.mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        this.mChart.setOnDragListener(null);
        setData(12,50);
        this.mChart.animateY(2500);
        Legend l = this.mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        FloatingActionButton fab = (FloatingActionButton)this.view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LessonManagerDatabase db = new LessonManagerDatabase(inflater.getContext());
                    List<Student> students = db.getStudents();
                    List<Lesson> lessons = db.getStudentLessons(students.get(0).getId());
                    listener.payForSomeone(students, lessons);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return this.view;
    }

    private void setData(int earnings, int credits) {

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Earnings");
        xVals.add("Credits");
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(earnings,0));
        yVals1.add(new BarEntry(credits,1));

        BarDataSet set1 = new BarDataSet(yVals1, "Cash");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        this.mChart.setData(data);
    }
}
