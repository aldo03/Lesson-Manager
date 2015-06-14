package com.example.matteoaldini.lessonmanager.utils;

import com.example.matteoaldini.lessonmanager.model.Student;

import java.util.List;

/**
 * Created by Filo on 14/06/2015.
 */
public class StringUtils {
    public static  String[] toStringArray(List<Student> list){
        String[] array = new String[list.size()];
        int i = 0;
        for(Student s: list){
            array[i] = s.toString();
            i++;
        }
        return array;
    }
}
