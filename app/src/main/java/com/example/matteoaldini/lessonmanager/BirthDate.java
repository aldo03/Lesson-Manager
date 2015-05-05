package com.example.matteoaldini.lessonmanager;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class BirthDate {
    private int day;
    private int month;
    private int year;

    public BirthDate(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public String toString(){
        return ""+this.day+"/"+this.month+"/"+this.year;
    }
}
