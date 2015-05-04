package com.example.matteoaldini.lessonmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public Object getItem(int position) {
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
        name.setText(studentList.get(position).getName()+" "+studentList.get(position).getSurname());
        this.roundedImage(convertView);
        return convertView;
    }

    private void roundedImage(View convertView){
        ImageView imageView1 = (ImageView) convertView.findViewById(R.id.person);
        Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.person);
        RoundImage roundedImage = new RoundImage(bm);
        imageView1.setImageDrawable(roundedImage);
    }
}
