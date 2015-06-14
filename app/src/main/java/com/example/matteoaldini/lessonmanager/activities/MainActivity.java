package com.example.matteoaldini.lessonmanager.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.fragments.CalendarFragment;
import com.example.matteoaldini.lessonmanager.fragments.CashGestureFragment;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.utils.StringUtils;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.adapters.TabAdapter;
import com.example.matteoaldini.lessonmanager.fragments.StudentListFragment;
import com.example.matteoaldini.lessonmanager.material_design.SlidingTabLayout;
import com.gc.materialdesign.views.ButtonFlat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




public class MainActivity extends ActionBarActivity implements StudentListFragment.StudentListListener, CalendarFragment.CalendarListener, CashGestureFragment.CashGestureListener {
    private static final int ADD_STUDENT_CODE = 9;
    private static final int DETAILS_STUDENT_CODE = 10;
    private static final String STUDENT_EXTRA = "student";
    private Toolbar toolbar;
    private ViewPager pager;
    private TabAdapter tabAdapter;
    private SlidingTabLayout tabs;
    private Intent addStudentIntent;
    private Intent detailsStudentIntent;
    private List<Student> studentsPayment;
    private List<Lesson> lessonsPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);

        pager = (ViewPager)findViewById(R.id.pager);
        this.tabAdapter = new TabAdapter(this.getSupportFragmentManager());
        pager.setAdapter(this.tabAdapter);

        // Assiging the Sliding Tab Layout View
        this.tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        this.tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        this.tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        this.tabs.setViewPager(pager);

        this.studentsPayment = new ArrayList<>();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        /*this.tabAdapter = new TabAdapter(this.getSupportFragmentManager());
        pager.setAdapter(this.tabAdapter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addNewStudent() {
        this.addStudentIntent = new Intent(this.getApplicationContext() ,AddOrEditStudentActivity.class);
        startActivityForResult(addStudentIntent, ADD_STUDENT_CODE);
    }

    @Override
    public void detailsStudent(Student s) {
        this.detailsStudentIntent = new Intent(this.getApplicationContext(), DetailsStudentActivity.class);
        this.detailsStudentIntent.putExtra(STUDENT_EXTRA,s);
        startActivityForResult(this.detailsStudentIntent, DETAILS_STUDENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==ADD_STUDENT_CODE){
                Toast.makeText(getApplicationContext(), R.string.student_added,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public List<Lesson> getLessons(Calendar date) {
        LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
        return db.getDateLessons(date);
    }

    @Override
    public void payForSomeone(final List<Student> students, List<Lesson> lessons) throws ParseException {
        LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
        this.studentsPayment = students;
        this.lessonsPayment = lessons;
        final Dialog dialogPayment = new Dialog(this);
        dialogPayment.setContentView(R.layout.custom_dialog_layout);
        dialogPayment.setTitle("Payment");
        final Spinner spinnerStudents = (Spinner)dialogPayment.findViewById(R.id.personlist);
        final Spinner spinnerLessons = (Spinner)dialogPayment.findViewById(R.id.numberLessons);
        ButtonFlat pay = (ButtonFlat)dialogPayment.findViewById(R.id.pay_button);
        ButtonFlat back = (ButtonFlat)dialogPayment.findViewById(R.id.back_button);

        String[] studentArray = StringUtils.toStringArrayStudents(students);
        ArrayAdapter<String> adapterStudent = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentArray);
        adapterStudent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(adapterStudent);
        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
                try {
                    lessonsPayment = db.getStudentLessons(studentsPayment.get(position).getId());
                    String[] lessonArray = StringUtils.generateLessonsArray(lessonsPayment.size());
                    ArrayAdapter<String> adapterLesson = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, lessonArray);
                    adapterLesson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLessons.setAdapter(adapterLesson);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] lessonArray = StringUtils.generateLessonsArray(lessons.size());
        ArrayAdapter<String> adapterLesson = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonArray);
        adapterLesson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessons.setAdapter(adapterLesson);

        dialogPayment.show();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int moneyToPay = 0;
                final int lessonsToBePaid = spinnerLessons.getSelectedItemPosition()+1;
                for(int i = 0; i<lessonsToBePaid; i++){
                    moneyToPay += lessonsPayment.get(i).getFare();
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Pay Lessons")
                        .setMessage("These lessons cost "+moneyToPay+"$, confirm?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
                                for(int i = 0; i<lessonsToBePaid; i++){
                                    Lesson l = lessonsPayment.get(i);
                                    l.setPaid(true);
                                    db.updateLesson(l);
                                    Toast.makeText(getApplicationContext(), R.string.lessons_paid, Toast.LENGTH_LONG).show();
                                }
                                dialogPayment.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .create()
                        .show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.cancel();
            }
        });
    }




}
