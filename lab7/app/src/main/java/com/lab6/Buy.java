package com.lab6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Buy extends AsyncTask<Params, Void, Void> {
    Context context;
    Products product;
    Context base;
    ListView scrollView;
    Boolean check = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Params... params) {
        context = params[0].getContext();
        product = params[0].getProduct();
        base = params[0].getBase();
        scrollView = params[0].getScrollView();
        int newCount = 0;

        SQLiteDatabase db = base.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT count FROM items where (id = " + product.getId() + ");", null);
        if (query.moveToFirst()) {
            do {
                newCount = query.getInt(0);
            }
            while (query.moveToNext());
        }

        newCount--;

        if (newCount < 0)
            check = true;
        else {
            db.execSQL("UPDATE items set count =" + newCount + " where id=" + product.getId());
            db.close();
            try {
                TimeUnit.SECONDS.sleep((new Random().nextInt(5) + 3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        int id;
        String name;
        int count;
        double price;
        ArrayList<Products> products = new ArrayList<Products>();

        super.onPostExecute(result);
        if (check) {
            String message = "Товар \"" + product.getName() + "\"" + " отсутствует в наличии.";
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            SQLiteDatabase db = base.openOrCreateDatabase("app.db", MODE_PRIVATE, null);

            Cursor query = db.rawQuery("SELECT * FROM items where (count > 0);", null);
            if (query.moveToFirst()) {
                do {
                    id = query.getInt(0);
                    name = query.getString(1);
                    count = query.getInt(2);
                    price = query.getDouble(3);
                    products.add(new Products(id, name, count, price));
                }
                while (query.moveToNext());
            }
            query.close();
            db.close();

            if (products.size() == 0) {
                String[] notification = {"Здесь пока ничего нет."};
                ArrayAdapter<String> adapter = new ArrayAdapter(context,
                        android.R.layout.simple_list_item_1, notification);
                scrollView.setAdapter(adapter);
            } else {
                Adapter adapter = new Adapter(context, base, products);
                scrollView.setAdapter(adapter);
            }
            String message = "Товар \"" + product.getName() + "\"" + " успешно куплен.";
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }



    }
}