package com.example.matteoaldini.lessonmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tabula rasa "+position;
    }
}
