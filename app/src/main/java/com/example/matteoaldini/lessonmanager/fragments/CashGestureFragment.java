package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.utils.StringUtils;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        updateChart();
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
    private Spinner studentSpinner;
    private List<Student> students;
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
    private LessonManagerDatabase db;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int earnings = 0;
        int credits = 0;
        db = new LessonManagerDatabase(getActivity());
        this.students = db.getStudents();
        this.view = inflater.inflate(R.layout.cash_gesture_layout, container, false);

        this.studentSpinner = (Spinner)this.view.findViewById(R.id.list_student_spinner);
        String[] studentArray = StringUtils.toStringArrayStudents(students ,true);
        studentArray[0] = "All";
        ArrayAdapter<String> adapterStudent = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, studentArray);
        adapterStudent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.studentSpinner.setAdapter(adapterStudent);

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
        this.dateEnd.setText("" + this.dayEnd + " / " + this.monthEnd + " / " + this.yearEnd);

        this.mChart = (HorizontalBarChart)this.view.findViewById(R.id.chart1);
        this.mChart.setDrawBarShadow(false);

        this.mChart.setDrawValueAboveBar(true);

        this.mChart.setDescription("");

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
        this.mChart.animateY(2500);
        Legend l = this.mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        this.updateChart();

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
        this.studentSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateChart();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)this.view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Earnings");
        xVals.add("Credits");
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        yVals1.add(new BarEntry(earnings,0));
        yVals1.add(new BarEntry(credits,1));

        BarDataSet set1 = new BarDataSet(yVals1, "Cash");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        int max = earnings > credits ? earnings : credits;
        max += 10;
        this.mChart.setMaxVisibleValueCount(max);

        this.mChart.setData(data);
    }

    private void updateChart(){
        Calendar dateBegin = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();
        int earnings = 0;
        int credits = 0;
        dateBegin.set(this.yearStart, this.monthStart, this.dayStart);
        dateEnd.set(this.yearEnd, this.monthEnd, this.dayEnd);
        if(this.studentSpinner.getSelectedItemPosition()==0){
            earnings = this.db.getOverallEarningsOrCredits(dateBegin, dateEnd, 1);
            credits = this.db.getOverallEarningsOrCredits(dateBegin, dateEnd, 0);
        }else{
            earnings = this.db.getOverAllEarningsOrCredits(dateBegin, dateEnd, 1, this.students.get(this.studentSpinner.getSelectedItemPosition()).getId());
            credits = this.db.getOverAllEarningsOrCredits(dateBegin, dateEnd, 0, this.students.get(this.studentSpinner.getSelectedItemPosition()).getId());
        }
        setData(earnings, credits);
    }

}
