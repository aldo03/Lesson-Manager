package com.example.matteoaldini.lessonmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.matteoaldini.lessonmanager.model.Lesson;
import com.example.matteoaldini.lessonmanager.model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by matteo.aldini on 05/05/2015.
 */



public class LessonManagerDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "newDB2";
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

        db.close();

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
        db.close();
        return students;
    }

    public Lesson insertNewLesson(Lesson lesson , int frequency, Calendar endDate){
        int step = 0;
        long result;
        Calendar day;
        day = lesson.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Log.i("SUBJECT",lesson.getSubject());
        if(frequency==0){
            Lesson lessonRet = this.checkLesson(lesson.getDate(), lesson.getHourStart(),
                    lesson.getMinStart(), lesson.getHourEnd(), lesson.getMinEnd(), -1);
            if(lessonRet!=null){
                return lessonRet;
            }
            result = this.addSingleLesson(lesson, sdf.format(day.getTime()));
            Log.i("", sdf.format(day.getTime())+"DAY OF WEEK:"+day.get(Calendar.DAY_OF_WEEK));
            return null;
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
        boolean conflict = false;  //if there are conflicts, lesson is not added but method doesn't stop
        while(day.compareTo(endDate)<0) {
            Log.i("", sdf.format(day.getTime())+"DAY OF WEEK:"+day.get(Calendar.DAY_OF_WEEK));
            Lesson lessonRet = this.checkLesson(day, lesson.getHourStart(),
                    lesson.getMinStart(), lesson.getHourEnd(), lesson.getMinEnd(), -1);
            if(lessonRet!=null){
                conflict = true;
            }else {
                result = this.addSingleLesson(lesson, sdf.format(day.getTime()));
            }
            day.add(Calendar.DAY_OF_MONTH,step);
        }
        if(conflict==true){
            return new Lesson();
        }else {
            return null;
        }
    }

    //checks if lesson to be put into database is in conflict with other lessons
    private Lesson checkLesson(Calendar date, int hourStart, int minStart, int hourEnd, int minEnd, long id){
        Lesson lesson;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateToFind = sdf.format(date.getTime());
        String query = "SELECT * FROM " + LESSONS_TABLE + " WHERE " + DATE_LESSON + "=?" +
                " AND NOT ((" + HOUR_START + ">=?" + " AND " + MIN_START + ">=?)" +
                " OR (" + HOUR_END + "<=?" + " AND " + MIN_END + "<=?))" +
                " AND "+ID_LESSON+"!=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{dateToFind, ""+hourEnd, ""+minEnd, ""+hourStart, ""+minStart, ""+id});
        if(cursor==null)
            return null;
        else{
            int indexDate = cursor.getColumnIndex(DATE_LESSON);
            int indexHourStart = cursor.getColumnIndex(HOUR_START);
            int indexMinStart = cursor.getColumnIndex(MIN_START);
            int indexHourEnd = cursor.getColumnIndex(HOUR_END);
            int indexMinEnd = cursor.getColumnIndex(MIN_END);
            int indexFare = cursor.getColumnIndex(FARE);
            int indexLocation = cursor.getColumnIndex(LOCATION);
            int indexSubject = cursor.getColumnIndex(SUBJECT_LESSON);
            int indexIdLesson = cursor.getColumnIndex(ID_LESSON);
            int indexPresent = cursor.getColumnIndex(LESSON_PRESENT);
            int indexPaid = cursor.getColumnIndex(LESSON_PAID);
            int indexIdStud = cursor.getColumnIndex(LESSON_STUDENT);

            if(!cursor.isAfterLast()){
                cursor.moveToNext();
                String dateL = cursor.getString(indexDate);
                int hourStartL = cursor.getInt(indexHourStart);
                int minStartL = cursor.getInt(indexMinStart);
                int hourEndL = cursor.getInt(indexHourEnd);
                int minEndL = cursor.getInt(indexMinEnd);
                int fare = cursor.getInt(indexFare);
                String location = cursor.getString(indexLocation);
                String subject = cursor.getString(indexSubject);
                long idLesson = cursor.getLong(indexIdLesson);
                int present = cursor.getInt(indexPresent);
                int paid = cursor.getInt(indexPaid);
                long idStud = cursor.getLong(indexIdStud);
                Student student = this.getStudentByID(idStud);
                lesson = new Lesson(student, getDateByString(dateL), hourStartL, minStartL, hourEndL, minEndL, fare, location, subject);
                lesson.setIdLesson(idLesson);
                lesson.setPaid(this.getBooleanByInt(paid));
                lesson.setPresent(this.getBooleanByInt(present));
                db.close();
                return lesson;
            }
            else{
                return null;
            }
        }
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

    public int getPayment(long idStudent, Calendar date, int hour, int min) {
        int cash = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateToFind = sdf.format(date.getTime());
        String query = "SELECT " + FARE + " FROM " + LESSONS_TABLE + " WHERE " + DATE_LESSON + "<=?" +
                " AND " + LESSON_PAID + "= 0" + " AND " + LESSON_PRESENT + "= 1" + " AND " + HOUR_START + "<=?" +
                " AND " + MIN_START + "<=?" + " AND " + LESSON_STUDENT + "=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{dateToFind, ""+hour, ""+min, ""+idStudent});
        if(cursor==null)
            return 0;
        while(cursor.moveToNext()){
            cash += cursor.getInt(0);
            Log.i("payment:",""+cash);
        }
        return cash;
    }

    public List<Lesson> getDateLessons(Calendar date) {
        List<Lesson> lessons = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
            lesson.setPaid(getBooleanByInt(paid));
            lesson.setPresent(getBooleanByInt(present));
            lesson.setIdLesson(idLesson);
            lessons.add(lesson);
            Log.i("lesson:",lesson.toString());
        }

        db.close();

        return lessons;
    }

    public List<Lesson> getStudentLessons(long idStud) throws ParseException {
        List<Lesson> lessons = new ArrayList<>();
        String query = "SELECT * FROM " + LESSONS_TABLE + " WHERE " + LESSON_STUDENT + "=?" + " AND " +
                LESSON_PAID + "=0" + " AND " + LESSON_PRESENT + "=1" + " ORDER BY date(" + DATE_LESSON + ")";
        SQLiteDatabase db = getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Cursor cursor = db.rawQuery(query, new String[]{""+idStud});

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
        int indexDate = cursor.getColumnIndex(DATE_LESSON);
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
            String date = cursor.getString(indexDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(date));
            long idLesson = cursor.getLong(indexIdLesson);
            int present = cursor.getInt(indexPresent);
            int paid = cursor.getInt(indexPaid);
            Student student = this.getStudentByID(idStud);
            Lesson lesson = new Lesson(student, calendar , hourStart, minStart, hourEnd, minEnd, fare, location, subject);
            lesson.setIdLesson(idLesson);
            lesson.setPaid(getBooleanByInt(paid));
            lesson.setPresent(getBooleanByInt(present));
            lessons.add(lesson);
        }

        db.close();
        return lessons;
    }

    public void updateStudent(String name, String surname, String phone, String mail, int color, long id){
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ""+ ID_STUDENT +"=?";

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(SURNAME, surname);
        values.put(PHONE, phone);
        values.put(EMAIL, mail);
        values.put(COLOR, color);

        db.update(STUDENTS_TABLE, values, whereClause , new String[]{""+id});

        db.close();
    }

    public Lesson updateLesson(Lesson modifiedLesson){
        Lesson lessonRet = this.checkLesson(modifiedLesson.getDate(), modifiedLesson.getHourStart(),
                modifiedLesson.getMinStart(), modifiedLesson.getHourEnd(), modifiedLesson.getMinEnd(), modifiedLesson.getId());
        if(lessonRet!=null){
            if(lessonRet.getId()!=modifiedLesson.getId())
                Log.i("","BUG");
                return lessonRet;
        }

        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ""+ ID_LESSON +"=?";

        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        values.put(DATE_LESSON, sdf.format(modifiedLesson.getDate().getTime()));
        values.put(HOUR_START, modifiedLesson.getHourStart());
        values.put(MIN_START, modifiedLesson.getMinStart());
        values.put(HOUR_END, modifiedLesson.getHourEnd());
        values.put(MIN_END, modifiedLesson.getMinEnd());
        values.put(FARE, modifiedLesson.getFare());
        values.put(LOCATION, modifiedLesson.getLocation());
        values.put(SUBJECT_LESSON, modifiedLesson.getSubject());
        values.put(LESSON_PRESENT, modifiedLesson.isPresent());
        values.put(LESSON_PAID, modifiedLesson.isPaid());

        db.update(LESSONS_TABLE, values, whereClause , new String[]{""+modifiedLesson.getId()});

        db.close();
        return null;
    }

    public void deleteStudent(long id){
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ""+ ID_STUDENT +"=?";

        db.delete(STUDENTS_TABLE,whereClause,new String[]{""+id});

        whereClause = ""+ LESSON_STUDENT +"=?";

        db.delete(LESSONS_TABLE,whereClause,new String[]{""+id});

        db.close();
    }

    public void deleteLesson(long id){
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ""+ ID_LESSON +"=?";

        db.delete(LESSONS_TABLE,whereClause,new String[]{""+id});

        db.close();
    }

    public Lesson getNextLesson(long idStud){
        Lesson lesson;

        Calendar day = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " +LESSONS_TABLE+ " WHERE " +LESSON_STUDENT+ "=? AND " +DATE_LESSON+
                " = (SELECT min(" +DATE_LESSON+") FROM " +LESSONS_TABLE+ " WHERE " +LESSON_STUDENT+ "=? AND( "+DATE_LESSON+">? OR("
                +DATE_LESSON+"=? AND "+HOUR_START+">=? OR("+DATE_LESSON+"=? AND "+HOUR_START+"=? AND "+MIN_START+">=?))))";
        Cursor cursor = db.rawQuery(query, new String[]{""+idStud, ""+idStud, sdf.format(day.getTime()),sdf.format(day.getTime()),
                ""+day.get(Calendar.HOUR),sdf.format(day.getTime()),""+day.get(Calendar.HOUR), ""+day.get(Calendar.MINUTE) });

        Log.i("",""+day.get(Calendar.HOUR));
        Log.i("",""+day.get(Calendar.MINUTE));
        if (cursor == null)
            return null;

        int indexDate = cursor.getColumnIndex(DATE_LESSON);
        int indexHourStart = cursor.getColumnIndex(HOUR_START);
        int indexMinStart = cursor.getColumnIndex(MIN_START);
        int indexHourEnd = cursor.getColumnIndex(HOUR_END);
        int indexMinEnd = cursor.getColumnIndex(MIN_END);
        int indexFare = cursor.getColumnIndex(FARE);
        int indexLocation = cursor.getColumnIndex(LOCATION);
        int indexSubject = cursor.getColumnIndex(SUBJECT_LESSON);
        int indexIdLesson = cursor.getColumnIndex(ID_LESSON);
        int indexPresent = cursor.getColumnIndex(LESSON_PRESENT);
        int indexPaid = cursor.getColumnIndex(LESSON_PAID);

        int hourMin=24;
        int minuteMin=61;
        Lesson nextLesson = new Lesson();
        boolean found = false;

        while(!(cursor.isLast()||cursor.isAfterLast())){
            cursor.moveToNext();
            Log.i("","iiii");
            String date = cursor.getString(indexDate);
            int hourStart = cursor.getInt(indexHourStart);
            int minStart = cursor.getInt(indexMinStart);

            int hourEnd = cursor.getInt(indexHourEnd);
            int minEnd = cursor.getInt(indexMinEnd);
            int fare = cursor.getInt(indexFare);
            String location = cursor.getString(indexLocation);
            String subject = cursor.getString(indexSubject);

            long idLesson = cursor.getLong(indexIdLesson);
            int present = cursor.getInt(indexPresent);
            int paid = cursor.getInt(indexPaid);
            Student student = this.getStudentByID(idStud);
            lesson = new Lesson(student, getDateByString(date), hourStart, minStart, hourEnd, minEnd, fare, location, subject);
            lesson.setIdLesson(idLesson);
            lesson.setPaid(getBooleanByInt(paid));
            lesson.setPresent(getBooleanByInt(present));
            if(!date.equals(sdf.format(day.getTime()))||hourStart>=day.get(Calendar.HOUR)||hourStart==day.get(Calendar.HOUR)&&minStart>=day.get(Calendar.MINUTE)) {
                if (hourStart < hourMin) {
                    hourMin = hourStart;
                    minuteMin = minStart;
                    nextLesson = lesson;
                    found = true;
                } else if (hourStart == hourMin) {
                    if (minStart < minuteMin) {
                        hourMin = hourStart;
                        minuteMin = minStart;
                        nextLesson = lesson;
                        found = true;
                    }
                }
            }

        }
        db.close();
        if(found){
            return nextLesson;
        }else{
            return null;
        }

    }

    public int getOverallEarningsOrCredits(Calendar dateStart, Calendar dateEnd, int creditOrEarning){  //1=E, 0=C
        int tot = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateStartToFind = sdf.format(dateStart.getTime());
        String dateEndToFind = sdf.format(dateEnd.getTime());

        String query = "SELECT sum("+FARE+") FROM "+LESSONS_TABLE+" WHERE "+LESSON_PAID+"=? AND "+DATE_LESSON+">? AND "+DATE_LESSON+"<?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{""+creditOrEarning, dateStartToFind, dateEndToFind});
        if(cursor==null)
            return 0;
        cursor.moveToNext();
        tot = cursor.getInt(0);
        db.close();
        return tot;
    }

    public int getOverAllEarningsOrCredits(Calendar dateStart, Calendar dateEnd, int creditOrEarning, long idStud){
        int tot = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateStartToFind = sdf.format(dateStart.getTime());
        String dateEndToFind = sdf.format(dateEnd.getTime());

        String query = "SELECT sum("+FARE+") FROM "+LESSONS_TABLE+" WHERE "+LESSON_PAID+"=? AND "+DATE_LESSON+">? AND "+DATE_LESSON+"<? AND "+LESSON_STUDENT+"=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{""+creditOrEarning, dateStartToFind, dateEndToFind, ""+idStud});
        if(cursor==null)
            return 0;
        cursor.moveToNext();
        tot = cursor.getInt(0);
        db.close();
        return tot;
    }

    public int getOverAllEarningsOrCredits(Calendar dateStart, Calendar dateEnd, int creditOrEarning, String subject){
        int tot = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateStartToFind = sdf.format(dateStart.getTime());
        String dateEndToFind = sdf.format(dateEnd.getTime());

        String query = "SELECT sum("+FARE+") FROM "+LESSONS_TABLE+" WHERE "+LESSON_PAID+"=? AND "+DATE_LESSON+">? AND "+DATE_LESSON+"<? AND "+SUBJECT_LESSON+"=?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{""+creditOrEarning, dateStartToFind, dateEndToFind, subject});
        if(cursor==null)
            return 0;
        cursor.moveToNext();
        tot = cursor.getInt(0);
        db.close();
        return tot;
    }

    private Calendar getDateByString(String date){
        Calendar retDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            retDate.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retDate;
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

    private boolean getBooleanByInt(int i){
        return i==0 ? false : true;
    }
}
