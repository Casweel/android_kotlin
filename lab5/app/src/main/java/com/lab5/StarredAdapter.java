package com.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StarredAdapter extends BaseAdapter {
    private ArrayList<String> strings;
    LayoutInflater lInflater;
    private  Context context;

    StarredAdapter(ArrayList<String> strings, Context context)
    {
        this.strings = strings;
        this.context = context;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public String getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.starredpost, parent, false);
        }
        String path = getItem(position);
        ImageView img = view.findViewById(R.id.starredMedia);
        Glide.with(context)
                .load(path)
                .into(img);

        return view;
    }

}