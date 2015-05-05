package com.example.matteoaldini.lessonmanager;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by Filo on 05/05/2015.
 */
public class Lesson {

    private Student student;
    private CalendarDay date;

    private boolean present;
    private boolean paid;

    public Lesson(CalendarDay date, Student student) {
        this.date = date;
        this.student = student;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public Student getStudent() {
        return student;
    }

    public CalendarDay getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isPaid() {
        return paid;
    }

}
