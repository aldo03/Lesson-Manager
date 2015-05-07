package com.example.matteoaldini.lessonmanager;

import java.util.Date;

/**
 * Created by Filo on 05/05/2015.
 */
public class Lesson {

    private Student student;
    private Date date;
    private MyTime timeStart;
    private Integer duration;
    private int fare;
    private String location;
    private int frequency;
    private boolean present;
    private boolean paid;

    public Lesson(Date date, Student student, Integer hour, Integer minute, Integer duration, int fare, String location, int frequency) {
        this.date = date;
        this.student = student;
        this.timeStart = new MyTime(hour, minute);
        this.duration = duration;
        this.fare = fare;
        this.location = location;
        this.frequency = frequency;
        this.paid = false;
        this.present = true;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimeStart(MyTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Student getStudent() {
        return student;
    }

    public Date getDate() {
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

    /* 0 if is only one lesson
     * 1 each week
     * 2 every two weeks
     * 3 every three weeks
     * 4..
     */

    public int getFrequency() {
        return frequency;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isPaid() {
        return paid;
    }

    public MyTime getTimeStart() {
        return timeStart;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
