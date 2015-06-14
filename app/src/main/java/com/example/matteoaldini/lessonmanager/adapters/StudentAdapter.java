package com.example.matteoaldini.lessonmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matteoaldini.lessonmanager.R;
import com.example.matteoaldini.lessonmanager.utils.ImageUtils;
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
        ImageUtils.setImageFromPosition(view, p);
        return convertView;
    }
}
