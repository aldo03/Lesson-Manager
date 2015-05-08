package com.example.matteoaldini.lessonmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.model.ImageUtils;
import com.example.matteoaldini.lessonmanager.model.Student;
import com.example.matteoaldini.lessonmanager.database.LessonManagerDatabase;

/**
 * Created by brando on 05/05/2015.
 */
public class AddStudentActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText email;
    private Spinner spinner;
    private ImageView image;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student);
        this.toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);
        this.name = (EditText) findViewById(R.id.nameAdd);
        this.surname = (EditText) findViewById(R.id.surnameAdd);
        this.phone = (EditText) findViewById(R.id.phoneAdd);
        this.email = (EditText) findViewById(R.id.emailAdd);
        this.spinner = (Spinner) findViewById(R.id.colorSpinner);
        this.image = (ImageView) findViewById(R.id.imageIconStudent);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageUtils.setImageFromPosition(image, position);
                color = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_item_menu){
            LessonManagerDatabase db = new LessonManagerDatabase(getApplicationContext());
            Student s = db.addNewStudent(this.name.getText().toString(),this.surname.getText().toString(),
                    this.phone.getText().toString(),this.email.getText().toString(), this.color);
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
