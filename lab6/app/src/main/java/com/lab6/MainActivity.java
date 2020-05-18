package com.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    Adapter adapter;
    Context context = this;
    ListView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView);
        updData();
    }

    public void toBackEnd(View v) {
        Intent intent = new Intent(this, BackEnd.class);
        startActivity(intent);
        db.close();
        finish();
    }

    public void onBuy(View view) {
        Button buy = (Button) view.findViewById(R.id.buy);
        Products product = (Products) buy.getTag();
        try {
            int newCount = product.getCount() - 1;
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            db.execSQL("UPDATE items set count =" + newCount + " where id=" + product.getId());
            String message = "Товар \"" + product.getName() + "\"" + " успешно куплен.";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            db.close();
        } finally {
            updData();
        }
    }

    public void updData() {
        int id;
        String name;
        int count;
        double price;
        ArrayList<Products> products = new ArrayList<Products>();

        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS items (id INTEGER primary key autoincrement, name TEXT, count INTEGER, price FLOAT)");


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
            ArrayAdapter<String> adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, notification);
            scrollView.setAdapter(adapter);
        } else {
            adapter = new Adapter(this, getBaseContext(), products);
            scrollView.setAdapter(adapter);

        }
    }
}
