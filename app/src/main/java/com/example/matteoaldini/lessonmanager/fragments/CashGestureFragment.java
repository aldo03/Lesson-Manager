package com.example.matteoaldini.lessonmanager.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.matteoaldini.lessonmanager.R;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;

/**
 * Created by Filo on 04/06/2015.
 */
public class CashGestureFragment extends Fragment {

    public interface CashGestureListener {

        void payForSomeone() throws ParseException;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof CashGestureListener){
            this.listener = (CashGestureListener)activity;
        }
    }

    private CashGestureListener listener;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.cash_gesture_layout, container, false);
        FloatingActionButton fab = (FloatingActionButton)this.view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.payForSomeone();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return this.view;
    }
}
