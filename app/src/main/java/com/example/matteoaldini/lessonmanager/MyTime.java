package com.example.matteoaldini.lessonmanager;


/**
 * Created by Filo on 05/05/2015.
 */
public class MyTime {

    private Integer minute;

    public MyTime(Integer hour, Integer minute) {
        this.minute = minute + hour*60;
    }

    public Integer getHour(){
        return minute>0?minute/60:null;
    }

    public Integer getMinute() {
        return minute>0?minute%60:null;
    }

    public void setMinute(Integer hour, Integer minute) {
        this.minute = minute + hour*60;
    }

    public String toString(){
        return getHour()+":"+getMinute();
    }
}
