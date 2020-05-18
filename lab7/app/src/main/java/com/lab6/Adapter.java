package com.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Adapter extends BaseAdapter {
    Context context;
    Context base;
    LayoutInflater lInflater;
   public ArrayList<Products> objects;

    Adapter(Context context, Context base, ArrayList<Products> products) {
        this.context = context;
        this.base = base;
        this.objects = products;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        Products product = getProduct(position);

        ((TextView) view.findViewById(R.id.name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.count)).setText(String.valueOf(product.getCount()));
        ((TextView) view.findViewById(R.id.price)).setText(String.valueOf(product.getPrice()));


        Button buy = (Button) view.findViewById(R.id.buy);
       // buy.setOnClickListener(OnButtonClick(context));
        buy.setTag(product);
        return view;
    }


    Products getProduct(int position) {
        return ((Products) getItem(position));
    }

}
