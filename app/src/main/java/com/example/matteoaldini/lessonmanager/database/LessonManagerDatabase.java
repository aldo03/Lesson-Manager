package com.example.matteoaldini.lessonmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matteoaldini.lessonmanager.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo.aldini on 05/05/2015.
 */



public class LessonManagerDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lesson_m_database";
    private static final String STUDENTS_TABLE = "students";
    private static final String LESSONS_TABLE = "lessons";

    private static final String ID_STUDENT = "id_student";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    //private static final String IMAGE = "image";

    private static final String DATE_TIME_START = "datelesson";
    private static final String DURATION = "duration";
    private static final String ID_LESSON = "id_lesson";
    private static final String LESSON_STUDENT = "lesson_student";



    private static final String CREATE_STUDENTS_TABLE =
            "CREATE TABLE " + STUDENTS_TABLE + " (" + ID_STUDENT + " INTEGER PRIMARY KEY,"
                    + NAME + " TEXT," + SURNAME + " TEXT," + PHONE + " TEXT," + EMAIL +" TEXT)";

    private static final String CREATE_LESSON_TABLE =
            "CREATE TABLE " + LESSONS_TABLE + " (" + ID_LESSON + " INTEGER PRIMARY KEY,"
                    + DATE_TIME_START + " TEXT," + DURATION + " INTEGER," + LESSON_STUDENT + " INTEGER, FOREIGN KEY("
                    + LESSON_STUDENT +") REFERENCES " + STUDENTS_TABLE + " (" + ID_STUDENT +")";

    public LessonManagerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);
    }

    public boolean addNewStudent(String name, String surname, String birthDate, String phone, String mail){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(SURNAME, surname);
        values.put(PHONE, phone);
        values.put(EMAIL, mail);

        long result = db.insert(STUDENTS_TABLE, null, values);

        if (result == -1)
            return false;
        return true;
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
            String phone = cursor.getString(cursor.getColumnIndex(PHONE));
            String email = cursor.getString(cursor.getColumnIndex(EMAIL));
            long id = cursor.getLong(cursor.getColumnIndex(ID_STUDENT));
            Student student = new Student(name,surname,phone,email);
            student.setId(id);
            students.add(student);
            cursor.moveToNext();
        }
        return students;
    }

    public void insertNewLesson(){

    }
}
