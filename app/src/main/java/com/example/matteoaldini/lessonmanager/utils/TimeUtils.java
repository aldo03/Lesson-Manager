package com.example.matteoaldini.lessonmanager.utils;

import android.widget.TextView;

/**
 * Created by brando on 02/06/2015.
 */
public class TimeUtils {
    public static void setTime(int hour, int minute, TextView t){
        String h="";
        String m="";
        if(hour<10){
            h="0";
        }
        if(minute<10){
            m="0";
        }

        t.setText(h+hour+":"+m+minute);
    }
}
