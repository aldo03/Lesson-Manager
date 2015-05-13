package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.model.ImageUtils;
import com.example.matteoaldini.lessonmanager.model.Lesson;

/**
 * Created by Famiglia Aldini on 12/05/2015.
 */
public class CardFragment extends Fragment {
    private Lesson l;
    private CardListener listener;

    public interface CardListener {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CardListener)
            listener = (CardListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lesson_layout, container, false);
        TextView info;
        TextView startHour;
        TextView endHour;
        CardView card;
        ImageView imgSubject;
        ImageView imgIconStudent;

        info = (TextView)view.findViewById(R.id.student_info);
        startHour = (TextView)view.findViewById(R.id.hour_info);
        endHour = (TextView)view.findViewById(R.id.hour_info2);
        imgSubject = (ImageView)view.findViewById(R.id.imageView);
        imgIconStudent = (ImageView)view.findViewById(R.id.userImage);
        card = (CardView)view.findViewById(R.id.card_view);

        info.setText(l.getStudent().getName()+" "+l.getStudent().getSurname());
        startHour.setText(getHourFromInt(l.getHourStart())+":"+getHourFromInt(l.getMinStart()));
        endHour.setText(getHourFromInt(l.getHourEnd()) + ":" + getHourFromInt(l.getMinEnd()));
        ImageUtils.setImageSubject(imgSubject, l.getSubject());
        ImageUtils.setImageFromPosition(imgIconStudent, l.getStudent().getColor());

        return view;
    }

    public void setLesson(Lesson lesson){
        this.l = lesson;
    }

    private String getHourFromInt(int time){
        if(time<10){
            return "0"+time;
        }else return ""+time;
    }
}

