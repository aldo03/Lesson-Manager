package com.example.matteoaldini.lessonmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.model.Student;

import java.util.List;

/**
 * Created by matteo.aldini on 04/05/2015.
 */
public class StudentAdapter extends BaseAdapter {
    private List<Student> studentList;
    private Context context;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.studentList!=null?this.studentList.size():0;
    }

    @Override
    public Student getItem(int position) {
        return this.studentList!=null?this.studentList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, null);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText(studentList.get(position).getName() + " " + studentList.get(position).getSurname());
        ImageView view = (ImageView) convertView.findViewById(R.id.studentIcon);
        int p = studentList.get(position).getColor();
        Log.i("COLOR", ""+p);
        setImage(view, p);
        return convertView;
    }

    private void setImage(ImageView image, int position){
        switch (position){
            case 0:
                image.setImageResource(R.drawable.person_blue_0f00b0);
                break;
            case 1:
                image.setImageResource(R.drawable.person_light_blue_0073bd);
                break;
            case 2:
                image.setImageResource(R.drawable.person_cyano_465974);
                break;
            case 3:
                image.setImageResource(R.drawable.person_dark_green_569a4a);
                break;
            case 4:
                image.setImageResource(R.drawable.person_green_3eb900);
                break;
            case 5:
                image.setImageResource(R.drawable.person_magenta_bb003c);
                break;
            case 6:
                image.setImageResource(R.drawable.person_olive_green_6abd00);
                break;
            case 7:
                image.setImageResource(R.drawable.person_orange_bd4a00);
                break;
            case 8:
                image.setImageResource(R.drawable.person_purple_bd00a9);
                break;
            case 9:
                image.setImageResource(R.drawable.person_red_bd0500);
                break;
            case 10:
                image.setImageResource(R.drawable.person_sand_756147);
                break;
            case 11:
                image.setImageResource(R.drawable.person_turquoise_00bda1);
                break;
            case 12:
                image.setImageResource(R.drawable.person_violet_5400bc);
                break;
            case 13:
                image.setImageResource(R.drawable.person_yellow_cbb81d);
                break;
            case 14:
                image.setImageResource(R.drawable.person_sky_blue_00b1bc);
                break;
        }
    }
}
