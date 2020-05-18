package com.lab6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Add extends AsyncTask<Params, Void, Void> {
    Context context;
    Context base;
    private String name;
    private Integer count;
    private Double price;
    private Spinner nameUpd;

    @Override
    protected Void doInBackground(Params... params) {
        context = params[0].getContext();
        base = params[0].getBase();
        name = params[0].getName();
        count = params[0].getCount();
        price = params[0].getPrice();
        nameUpd = params[0].getNameUpd();

        SQLiteDatabase db = base.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS items (id INTEGER primary key autoincrement, name TEXT, count INTEGER, price FLOAT)");
        db.execSQL("INSERT INTO items ( name , count, price ) VALUES (\"" + name + "\", " + count + ", " + price + " )");
        db.close();

        try {
            TimeUnit.SECONDS.sleep((new Random().nextInt(5) + 3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        ArrayList<String> names = new ArrayList<String>();
        SQLiteDatabase db = base.openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("Select name from items", null);
        if (query.moveToFirst()) {
            do {
                names.add(query.getString(0));
            }
            while (query.moveToNext());
        }
        query.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameUpd.setAdapter(adapter);

        String message = "Товар \"" + name + "\"" + "добавлен.";
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}