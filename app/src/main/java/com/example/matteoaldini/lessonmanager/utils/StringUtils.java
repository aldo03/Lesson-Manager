package com.example.matteoaldini.lessonmanager.utils;

import com.example.matteoaldini.lessonmanager.model.Student;

import java.util.List;

/**
 * Created by Filo on 14/06/2015.
 */
public class StringUtils {
    public static  String[] toStringArrayStudents(List<Student> list, boolean all){
        String[] array = new String[list.size()];
        int i = all? 1 : 0;
        for(Student s: list){
            array[i] = s.toString();
            i++;
        }
        return array;
    }

    public static String[] generateLessonsArray(int num){
        String[] values = new String[num];
        for(int i=0; i<num; i++){
            values[i]=""+(i+1);
        }
        return values;
    }
}
