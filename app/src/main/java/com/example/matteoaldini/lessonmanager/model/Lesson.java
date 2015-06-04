package com.example.matteoaldini.lessonmanager.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Filo on 05/05/2015.
 */
public class Lesson implements Serializable{
    private Student student;
    private Calendar date;
    private int hourStart;
    private int minStart;
    private int hourEnd;
    private int minEnd;
    private int fare;
    private String location;
    private String subject;
    private boolean present;
    private boolean paid;
    private long idLesson;

    public Lesson(){

    }

    public Lesson(Student student, Calendar date, int hourStart, int minStart, int hourEnd, int minEnd,
                  int fare, String location, String subject) {
        this.student = student;
        this.date = date;
        this.hourStart = hourStart;
        this.minStart = minStart;
        this.hourEnd = hourEnd;
        this.minEnd = minEnd;
        this.fare = fare;
        this.location = location;
        this.present = true;
        this.paid = false;
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getHourStart() {
        return hourStart;
    }

    public void setHourStart(int hourStart) {
        this.hourStart = hourStart;
    }

    public int getMinStart() {
        return minStart;
    }

    public void setMinStart(int minStart) {
        this.minStart = minStart;
    }

    public int getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(int hourEnd) {
        this.hourEnd = hourEnd;
    }

    public int getMinEnd() {
        return minEnd;
    }

    public void setMinEnd(int minEnd) {
        this.minEnd = minEnd;
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

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setIdLesson(long id){
        this.idLesson = id;
    }

    public String getSubject() {
        return subject;
    }

    public long getId() {
        return idLesson;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "student=" + student +
                ", date=" + date +
                ", hourStart=" + hourStart +
                ", minStart=" + minStart +
                ", hourEnd=" + hourEnd +
                ", minEnd=" + minEnd +
                ", fare=" + fare +
                ", location='" + location + '\'' +
                ", subject='" + subject + '\'' +
                ", present=" + present +
                ", paid=" + paid +
                ", idLesson=" + idLesson +
                '}';
    }


}
