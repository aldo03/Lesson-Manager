package com.example.matteoaldini.lessonmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matteoaldini.lessonmanager.BirthDate;
import com.example.matteoaldini.lessonmanager.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo.aldini on 05/05/2015.
 */



public class StudentsDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "students_db";
    private static final String STUDENTS_TABLE = "students";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String BIRTHDATE = "birthdate";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    //private static final String IMAGE = "image";

    private static final String CREATE_STUDENTS_TABLE =
            "CREATE TABLE " + STUDENTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY,"
                    + NAME + " TEXT," + SURNAME + " TEXT," + BIRTHDATE + " TEXT," + PHONE + " TEXT," + EMAIL +" TEXT)";


    public StudentsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    public Student addNewStudent(Student student){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, student.getName());
        values.put(SURNAME, student.getSurname());
        values.put(BIRTHDATE, student.getBirthDateString());
        values.put(PHONE, student.getPhone());
        values.put(EMAIL, student.getEmail());

        long result = db.insert(STUDENTS_TABLE, null, values);

        if (result == -1)
            return null;

        student.setId(result);
        return student;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM " + STUDENTS_TABLE;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || !cursor.moveToFirst())
            return null;

        for (int i = 0; i < cursor.getCount(); i++) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String surname = cursor.getString(cursor.getColumnIndex(SURNAME));
            BirthDate birthDate = parseDate(cursor.getString(cursor.getColumnIndex(BIRTHDATE)));
            String phone = cursor.getString(cursor.getColumnIndex(PHONE));
            String email = cursor.getString(cursor.getColumnIndex(EMAIL));
            long id = cursor.getLong(cursor.getColumnIndex(ID));
            Student student = new Student(name,surname,birthDate,phone,email);
            student.setId(id);
            students.add(student);
            cursor.moveToNext();
        }
        return students;
    }

    private BirthDate parseDate(String date){
        BirthDate birthDate;
        String[] s = date.split("/");
        birthDate = new BirthDate(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));
        return birthDate;
    }
}
