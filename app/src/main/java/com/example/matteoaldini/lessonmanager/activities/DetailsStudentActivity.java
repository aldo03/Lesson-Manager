package com.example.matteoaldini.lessonmanager.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;
import com.example.matteoaldini.lessonmanager.fragments.CardFragment;
import com.example.matteoaldini.lessonmanager.model.ImageUtils;
import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Famiglia Aldini on 09/05/2015.
 */
public class DetailsStudentActivity extends ActionBarActivity implements CardFragment.CardListener {
    private Toolbar toolbar;
    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView phone;
    private RelativeLayout layout;
    private ImageView image;
    private FloatingActionButton button;
    private Student student;
    private Lesson l;
    private CardFragment cardFragment;
    private static final int ADD_LESSON_CODE = 6;
    private static final int EDIT_STUDENT_CODE = 7;
    private static final String STUDENT_KEY = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.student = (Student) intent.getSerializableExtra(STUDENT_KEY);
        LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
        this.l = db.getNextLesson(student.getId());
        if(l!=null){
            this.l.getDate();
            cardFragment = new CardFragment();
            cardFragment.setLesson(l);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.layoutCard, cardFragment, "frag");
            transaction.commit();
        }

        setContentView(R.layout.details_student_layout);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);
        this.name = (TextView) findViewById(R.id.nameDetails);
        this.surname = (TextView) findViewById(R.id.surnameDetails);
        this.email = (TextView) findViewById(R.id.mailDetails);
        this.phone = (TextView) findViewById(R.id.phoneDetails);
        this.layout = (RelativeLayout) findViewById(R.id.layoutColor);
        this.image = (ImageView) findViewById(R.id.imageDetail);
        this.button = (FloatingActionButton) findViewById(R.id.fab);
        this.initFields();
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddLesson = new Intent(getApplicationContext(), AddLessonActivity.class);
                intentAddLesson.putExtra(STUDENT_KEY, student);
                startActivityForResult(intentAddLesson, ADD_LESSON_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_student,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            if (requestCode == ADD_LESSON_CODE) {
                Toast.makeText(getApplicationContext(), R.string.lesson_added, Toast.LENGTH_SHORT).show();
            } else if(requestCode == EDIT_STUDENT_CODE){
                Toast.makeText(getApplicationContext(), R.string.student_edited, Toast.LENGTH_SHORT).show();
                this.student = (Student)data.getSerializableExtra(STUDENT_KEY);
                this.initFields();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit_item_menu){
            Intent intentEditStudent = new Intent(getApplicationContext(), AddOrEditStudentActivity.class);
            intentEditStudent.putExtra(STUDENT_KEY, this.student);
            startActivityForResult(intentEditStudent, EDIT_STUDENT_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields(){
        this.toolbar.setBackgroundColor(ImageUtils.getDarkColor(this.student.getColor(), getApplicationContext()));
        this.button.setColorNormal(ImageUtils.getDarkColor(this.student.getColor(), getApplicationContext()));
        this.button.setColorPressed(ImageUtils.getDarkColor(this.student.getColor(), getApplicationContext()));
        this.button.setColorRipple(ImageUtils.getDarkColor(this.student.getColor(), getApplicationContext()));
        this.name.setText(student.getName());
        this.surname.setText(student.getSurname());
        this.email.setText(student.getEmail());
        this.phone.setText(student.getPhone());
        ImageUtils.setLayoutColor(this.layout, student.getColor(), this);
        ImageUtils.setImageFromPosition(this.image, student.getColor());
    }
}
