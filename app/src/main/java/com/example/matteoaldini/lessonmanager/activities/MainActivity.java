package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.fragments.CalendarFragment;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.adapters.TabAdapter;
import com.example.matteoaldini.lessonmanager.fragments.StudentListFragment;
import com.example.matteoaldini.lessonmanager.material_design.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity implements StudentListFragment.StudentListListener, CalendarFragment.CalendarListener {
    private static final int ADD_STUDENT_CODE = 9;
    private static final int DETAILS_STUDENT_CODE = 10;
    private static final String STUDENT_EXTRA = "student";
    private Toolbar toolbar;
    private ViewPager pager;
    private TabAdapter tabAdapter;
    private SlidingTabLayout tabs;
    private Intent addStudentIntent;
    private Intent detailsStudentIntent;

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
        this.addStudentIntent = new Intent(this.getApplicationContext() ,AddStudentActivity.class);
        startActivityForResult(addStudentIntent, ADD_STUDENT_CODE);
    }

    @Override
    public void detailsStudent(Student s) {
        this.detailsStudentIntent = new Intent(this.getApplicationContext(), AddLessonActivity.class);
        this.detailsStudentIntent.putExtra(STUDENT_EXTRA,s);
        startActivityForResult(this.detailsStudentIntent, DETAILS_STUDENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==ADD_STUDENT_CODE){
                Toast.makeText(getApplicationContext(), R.string.student_added,Toast.LENGTH_SHORT).show();
            }else if(requestCode==DETAILS_STUDENT_CODE){
                Toast.makeText(getApplicationContext(), R.string.lesson_added,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public List<Lesson> getLessons(Calendar date) {
        List<Lesson> list = new ArrayList<>();
        return list;
    }
}
