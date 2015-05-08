package com.example.matteoaldini.lessonmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by matteo.aldini on 05/05/2015.
 */



public class LessonManagerDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "LSSMdb";
    private static final String STUDENTS_TABLE = "students";
    private static final String LESSONS_TABLE = "lessons";

    private static final String ID_STUDENT = "id_student";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String PHONE = "phone";
    private static final String COLOR = "color";
    private static final String EMAIL = "email";
    //private static final String IMAGE = "image";

    private static final String DATE_LESSON = "datelesson";
    private static final String HOUR_START = "hour_start";
    private static final String MIN_START = "min_start";
    private static final String HOUR_END = "hour_end";
    private static final String MIN_END = "min_end";
    private static final String FARE = "fare";
    private static final String LOCATION = "location";
    private static final String SUBJECT_LESSON = "subject_lesson";
    private static final String ID_LESSON = "id_lesson";
    private static final String LESSON_STUDENT = "lesson_student";
    private static final String LESSON_PRESENT = "lesson_present";
    private static final String LESSON_PAID = "lesson_paid";



    private static final String CREATE_STUDENTS_TABLE =
            "CREATE TABLE " + STUDENTS_TABLE + " (" + ID_STUDENT + " INTEGER PRIMARY KEY,"
                    + NAME + " TEXT," + SURNAME + " TEXT," + PHONE + " TEXT," + COLOR + " INTEGER," + EMAIL +" TEXT)";

    private static final String CREATE_LESSON_TABLE =
            "CREATE TABLE " + LESSONS_TABLE + " (" + ID_LESSON + " INTEGER PRIMARY KEY,"
                    + DATE_LESSON + " TEXT," + LOCATION + " TEXT," + SUBJECT_LESSON + " TEXT," + HOUR_START + " INTEGER," + MIN_START + " INTEGER,"
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

    public Student addNewStudent(String name, String surname, String phone, String mail, int color){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(SURNAME, surname);
        values.put(PHONE, phone);
        values.put(EMAIL, mail);
        values.put(COLOR, color);

        long result = db.insert(STUDENTS_TABLE, null, values);

        Student s = new Student(name, surname, phone, mail, color);
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

        int indexName = cursor.getColumnIndex(NAME);
        int indexSurname = cursor.getColumnIndex(SURNAME);
        int indexPhone = cursor.getColumnIndex(PHONE);
        int indexEmail = cursor.getColumnIndex(EMAIL);
        int indexColor = cursor.getColumnIndex(COLOR);
        int indexID = cursor.getColumnIndex(ID_STUDENT);

        for (int i = 0; i < cursor.getCount(); i++) {
            String name = cursor.getString(indexName);
            String surname = cursor.getString(indexSurname);
            String phone = cursor.getString(indexPhone);
            String email = cursor.getString(indexEmail);
            int color = cursor.getInt(indexColor);
            long id = cursor.getLong(indexID);
            Student student = new Student(name,surname,phone,email, color);
            student.setId(id);
            students.add(student);
            cursor.moveToNext();
        }
        return students;
    }

    public boolean insertNewLesson(Lesson lesson , int frequency, Calendar endDate){
        int step = 0;
        long result;
        Calendar day;
        day = lesson.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
         Log.i("SUBJECT",lesson.getSubject());
        if(frequency==0){
            result = this.addSingleLesson(lesson, sdf.format(day.getTime()));
            Log.i("", sdf.format(day.getTime())+"DAY OF WEEK:"+day.get(Calendar.DAY_OF_WEEK));
            if (result == -1)
                return false;
            else return true;
        }
        switch (frequency){
            case 1:
                step = 7;
                break;
            case 2:
                step = 14;
                break;
            case 3:
                step = 21;
                break;
            case 4:
                step = 28;
                break;
        }
        while(day.compareTo(endDate)<0) {
            Log.i("", sdf.format(day.getTime())+"DAY OF WEEK:"+day.get(Calendar.DAY_OF_WEEK));
            result = this.addSingleLesson(lesson, sdf.format(day.getTime()));
            day.add(Calendar.DAY_OF_MONTH,step);
            if (result == -1)
                return false;
        }
        return true;
    }

    private long addSingleLesson(Lesson lesson, String date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_LESSON, date);
        values.put(HOUR_START, lesson.getHourStart());
        values.put(MIN_START, lesson.getMinStart());
        values.put(HOUR_END, lesson.getHourEnd());
        values.put(MIN_END, lesson.getMinEnd());
        values.put(FARE, lesson.getFare());
        values.put(LOCATION, lesson.getLocation());
        values.put(SUBJECT_LESSON, lesson.getSubject());
        values.put(LESSON_STUDENT, lesson.getStudent().getId());
        values.put(LESSON_PRESENT, lesson.isPresent());
        values.put(LESSON_PAID, lesson.isPaid());
        return(db.insert(LESSONS_TABLE, null, values));
    }

    public List<Lesson> getDateLessons(Calendar date) {
        List<Lesson> lessons = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateToFind = sdf.format(date.getTime());
        String query = "SELECT * FROM " + LESSONS_TABLE + " WHERE " + DATE_LESSON + "=?";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[]{dateToFind});

        if (cursor == null)
            return null;
        int indexHourStart = cursor.getColumnIndex(HOUR_START);
        int indexMinStart = cursor.getColumnIndex(MIN_START);
        int indexHourEnd = cursor.getColumnIndex(HOUR_END);
        int indexMinEnd = cursor.getColumnIndex(MIN_END);
        int indexFare = cursor.getColumnIndex(FARE);
        int indexLocation = cursor.getColumnIndex(LOCATION);
        int indexSubject = cursor.getColumnIndex(SUBJECT_LESSON);
        int indexIdLesson = cursor.getColumnIndex(ID_LESSON);
        int indexStudent = cursor.getColumnIndex(LESSON_STUDENT);
        int indexPresent = cursor.getColumnIndex(LESSON_PRESENT);
        int indexPaid = cursor.getColumnIndex(LESSON_PAID);

        while(cursor.moveToNext()){
            int hourStart = cursor.getInt(indexHourStart);
            int minStart = cursor.getInt(indexMinStart);
            int hourEnd = cursor.getInt(indexHourEnd);
            int minEnd = cursor.getInt(indexMinEnd);
            int fare = cursor.getInt(indexFare);
            String location = cursor.getString(indexLocation);
            String subject = cursor.getString(indexSubject);
            long idStud = cursor.getLong(indexStudent);
            long idLesson = cursor.getLong(indexIdLesson);
            int present = cursor.getInt(indexPresent);
            int paid = cursor.getInt(indexPaid);
            Student student = this.getStudentByID(idStud);
            Lesson lesson = new Lesson(student, date, hourStart, minStart, hourEnd, minEnd, fare, location, subject);
            lesson.setIdLesson(idLesson);
            lessons.add(lesson);
            Log.i("lesson:",lesson.toString());
        }

        return lessons;
    }

    private Student getStudentByID(long id){
        String query = "SELECT * FROM " + STUDENTS_TABLE + " WHERE " + ID_STUDENT + "=?";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[]{""+id});

        cursor.moveToNext();

        int indexName = cursor.getColumnIndex(NAME);
        int indexSurname = cursor.getColumnIndex(SURNAME);
        int indexPhone = cursor.getColumnIndex(PHONE);
        int indexEmail = cursor.getColumnIndex(EMAIL);
        int indexColor = cursor.getColumnIndex(COLOR);

        String name = cursor.getString(indexName);
        String surname = cursor.getString(indexSurname);
        String phone = cursor.getString(indexPhone);
        String email = cursor.getString(indexEmail);
        int color = cursor.getInt(indexColor);
        Student student = new Student(name,surname,phone,email, color);
        student.setId(id);
        return student;
    }
}
