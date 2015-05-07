package com.example.matteoaldini.lessonmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matteoaldini.lessonmanager.Lesson;
import com.example.matteoaldini.lessonmanager.Student;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
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

    private static final String DATE_LESSON = "datelesson";
    private static final String HOUR_START = "hour_start";
    private static final String MIN_START = "min_start";
    private static final String HOUR_END = "hour_end";
    private static final String MIN_END = "min_end";
    private static final String FARE = "fare";
    private static final String LOCATION = "location";
    private static final String ID_LESSON = "id_lesson";
    private static final String LESSON_STUDENT = "lesson_student";
    private static final String LESSON_PRESENT = "lesson_present";
    private static final String LESSON_PAID = "lesson_paid";



    private static final String CREATE_STUDENTS_TABLE =
            "CREATE TABLE " + STUDENTS_TABLE + " (" + ID_STUDENT + " INTEGER PRIMARY KEY,"
                    + NAME + " TEXT," + SURNAME + " TEXT," + PHONE + " TEXT," + EMAIL +" TEXT)";

    private static final String CREATE_LESSON_TABLE =
            "CREATE TABLE " + LESSONS_TABLE + " (" + ID_LESSON + " INTEGER PRIMARY KEY,"
                    + DATE_LESSON + " DATE," + LOCATION + " TEXT," + HOUR_START + " INTEGER," + MIN_START + " INTEGER,"
                    + HOUR_END + " INTEGER," + MIN_END + " INTEGER," + FARE + " INTEGER," + LESSON_PRESENT + " INTEGER,"
                    + LESSON_PAID + " INTEGER,"+ LESSON_STUDENT + " INTEGER, FOREIGN KEY("
                    + LESSON_STUDENT +") REFERENCES " + STUDENTS_TABLE + " (" + ID_STUDENT +"))";

    public LessonManagerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);
    }

    public Student addNewStudent(String name, String surname, String phone, String mail){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(SURNAME, surname);
        values.put(PHONE, phone);
        values.put(EMAIL, mail);

        long result = db.insert(STUDENTS_TABLE, null, values);

        Student s = new Student(name, surname, phone, mail);
        s.setId(result);

        if (result == -1)
            return null;
        return s;
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

    public boolean insertNewLesson(Lesson lesson , int frequency, Calendar endDate){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_LESSON, lesson.getDate().toString());
        values.put(HOUR_START, lesson.getHourStart());
        values.put(MIN_START, lesson.getMinStart());
        values.put(HOUR_END, lesson.getHourEnd());
        values.put(MIN_END, lesson.getMinEnd());
        values.put(FARE, lesson.getFare());
        values.put(LOCATION, lesson.getLocation());
        values.put(LESSON_STUDENT, lesson.getStudent().getId());
        values.put(LESSON_PRESENT, lesson.isPresent());
        values.put(LESSON_PAID, lesson.isPaid());

        long result = db.insert(LESSONS_TABLE, null, values);

        if (result == -1)
            return false;
        return true;
    }
}
