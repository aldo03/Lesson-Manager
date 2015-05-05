package com.example.matteoaldini.lessonmanager;

import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * Created by Filo on 05/05/2015.
 */
public class Lesson {

    private Student student;
    private CalendarDay date;
    private MyTime timeStart;
    private Integer duration;
    private boolean present;
    private boolean paid;

    public Lesson(CalendarDay date, Student student, Integer hour, Integer minute, Integer duration) {
        this.date = date;
        this.student = student;
        this.timeStart = new MyTime(hour, minute);
        this.duration = duration;
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

    public Integer getHourBegin(){
        return this.timeStart.getHour();
    }

    public Integer getMinuteBegin(){
        return this.timeStart.getMinute();
    }

    public Integer getDuration() {
        return duration;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isPaid() {
        return paid;
    }

}
