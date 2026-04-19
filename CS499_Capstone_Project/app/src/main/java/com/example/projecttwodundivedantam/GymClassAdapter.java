package com.example.projecttwodundivedantam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GymClassAdapter extends BaseAdapter {

    private Context context;
    private List<GymClass> classes;

    public GymClassAdapter(Context context, List<GymClass> classes) {
        this.context = context;
        this.classes = classes;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int position) {
        return classes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return classes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_gym_class, parent, false);
        }

        GymClass gymClass = classes.get(position);
        TextView textInfo = view.findViewById(R.id.textClassInfo);

        String info = "ID: " + gymClass.getId() + "\n" +
                gymClass.getTime() + " - " + gymClass.getName() + "\n" +
                "Instructor: " + gymClass.getInstructor() +
                ", Mat: " + gymClass.getMat();

        textInfo.setText(info);

        return view;
    }

    public void updateData(List<GymClass> newClasses) {
        this.classes = newClasses;
        notifyDataSetChanged();
    }
}
