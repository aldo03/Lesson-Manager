package com.example.matteoaldini.lessonmanager;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.matteoaldini.lessonmanager.database.StudentsDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class StudentListFragment extends android.support.v4.app.Fragment {
    private static final int REQUEST_CODE = 9;
    private View view;
    private StudentAdapter studAdapter;
    private ListView list;
    private Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.students_list_layout, container, false);
        this.studAdapter = new StudentAdapter(getActivity(), this.loadStudents(getActivity()));
        this.list = (ListView) this.view.findViewById(R.id.studentListId);
        this.list.setAdapter(this.studAdapter);
        FloatingActionButton fab = (FloatingActionButton) this.view.findViewById(R.id.fab);
        fab.attachToListView(this.list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(view.getContext() ,AddStudentActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return this.view;
    }

    private List<Student> loadStudents(Context context) {
        StudentsDatabase db = new StudentsDatabase(context);
        List<Student> listStud = db.getStudents();
        return listStud;
    }

    /*private List<Student> generateStudents(){
        List<Student> list = new ArrayList<>();
        BirthDate b1 = new BirthDate(3,3,2001);
        BirthDate b2 = new BirthDate(5,6,2004);
        BirthDate b3 = new BirthDate(7,10,2000);
        list.add(new Student("Matteo","Aldini",b1,"33334343","matteo.aldini@gmail.com"));
        list.add(new Student("Brando","Mordenti",b2,"3455645646","brando.mordenti@gmail.com"));
        list.add(new Student("Filippo","Berlini",b3,"322446463","filippo.berlini@gmail.com"));
        list.add(new Student("Filippo","Berlini",b3,"322446463","filippo.berlini@gmail.com"));
        list.add(new Student("Filippo","Berlini",b3,"322446463","filippo.berlini@gmail.com"));
        return list;
    }


}
