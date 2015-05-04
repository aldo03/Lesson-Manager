package com.example.matteoaldini.lessonmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class CustomFragment extends android.support.v4.app.Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.students_list_layout, container, false);

        return view;
    }
}
