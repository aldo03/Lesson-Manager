package com.example.matteoaldini.lessonmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.matteoaldini.lessonmanager.fragments.CalendarFragment;
import com.example.matteoaldini.lessonmanager.fragments.StudentListFragment;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = new Fragment();
        switch(i){
            case 0:
                StudentListFragment fragment = new StudentListFragment();
                f = fragment;
            break;
            case 1:
                CalendarFragment calFragment = new CalendarFragment();
                f =  calFragment;
            break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "STUDENTS";
            case 1:
                return "LESSONS";
            case 2:
                return "CASH";
            default:
                return "";
        }
    }
}
