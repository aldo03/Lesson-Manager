package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.utils.StringUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
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
            if(year>yearEnd||(year==yearEnd&&month>monthEnd)||(year==yearEnd&&month==monthEnd&&day>=dayEnd)){
                Toast.makeText(getActivity(), R.string.wrong_end_date,Toast.LENGTH_LONG).show();
            }else {
                this.yearStart = year;
                this.monthStart = month;
                this.dayStart = day;
                this.dateStart.setText("" + this.dayStart + " / " + (this.monthStart+1) + " / " + this.yearStart);
            }
        }else {
            if(year<yearStart||(year==yearStart&&month<monthStart)||(year==yearStart&&month==monthStart&&day<=dayStart)){
                Toast.makeText(getActivity(), R.string.wrong_end_date,Toast.LENGTH_LONG).show();
            }else {
                this.yearEnd = year;
                this.monthEnd = month;
                this.dayEnd = day;
                this.dateEnd.setText("" + this.dayEnd + " / " + (this.monthEnd+1) + " / " + this.yearEnd);
            }
        }
        this.updateChart();
        this.pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        this.setPieData();
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
    protected HorizontalBarChart horizontalBarChart;
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
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        this.horizontalBarChart = (HorizontalBarChart)this.view.findViewById(R.id.chart1);
        this.horizontalBarChart.setDrawBarShadow(false);

        this.horizontalBarChart.setDrawValueAboveBar(true);

        this.horizontalBarChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
        this.horizontalBarChart.setPinchZoom(false);

        this.horizontalBarChart.setDrawGridBackground(false);
        XAxis xl = this.horizontalBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = this.horizontalBarChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        YAxis yr = this.horizontalBarChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        this.horizontalBarChart.animateY(2500);
        Legend l = this.horizontalBarChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        this.pieChart = (PieChart) this.view.findViewById(R.id.chart2);
        this.pieChart.setUsePercentValues(true);
        this.pieChart.setDescription("");

        this.pieChart.setDragDecelerationFrictionCoef(0.95f);

        this.pieChart.setDrawHoleEnabled(true);
        this.pieChart.setHoleColorTransparent(true);

        this.pieChart.setTransparentCircleColor(Color.WHITE);

        this.pieChart.setHoleRadius(58f);
        this.pieChart.setTransparentCircleRadius(61f);

        this.pieChart.setDrawCenterText(true);

        this.pieChart.setRotationEnabled(false);


        this.pieChart.setCenterText("Subject earnings");
        this.pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        this.setPieData();

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
        this.studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateChart();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)this.view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Lesson> lessons = db.getStudentLessonsPaid(students.get(0).getId());
                    listener.payForSomeone(students, lessons);
                    updateChart();
                    pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
                    setPieData();
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
        this.horizontalBarChart.setMaxVisibleValueCount(max);

        this.horizontalBarChart.setData(data);
        this.horizontalBarChart.invalidate();
    }

    private void setPieData() {
        Calendar dateBegin = Calendar.getInstance();
        Calendar dateEnd = Calendar.getInstance();
        dateBegin.set(this.yearStart, this.monthStart, this.dayStart);
        dateEnd.set(this.yearEnd, this.monthEnd, this.dayEnd);
        String[] subjects = getResources().getStringArray(R.array.subjects);
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for(String str: subjects){
            int i = 0;
            int earned = db.getOverAllEarningsOrCredits(dateBegin,dateEnd,1,str);
            if(earned!=0){
                yVals.add(new Entry((float) earned,i));
                xVals.add(i,str);
                i++;
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Cash Percentual");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        this.pieChart.setData(data);

        // undo all highlights
        this.pieChart.highlightValues(null);
        this.pieChart.invalidate();
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
            earnings = this.db.getOverAllEarningsOrCredits(dateBegin, dateEnd, 1, this.students.get(this.studentSpinner.getSelectedItemPosition()-1).getId());
            credits = this.db.getOverAllEarningsOrCredits(dateBegin, dateEnd, 0, this.students.get(this.studentSpinner.getSelectedItemPosition()-1).getId());
        }
        this.horizontalBarChart.animateY(2500);
        setData(earnings, credits);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateChart();
        this.pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        this.setPieData();
    }
}
