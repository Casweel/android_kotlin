package com.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BackEnd extends AppCompatActivity {
    EditText nameAdd;
    Spinner countAdd;
    EditText priceAdd;
    Spinner nameUpd;
    Spinner countUpd;
    EditText priceUpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_end);
        nameAdd = (EditText) findViewById(R.id.nameAdd);
        countAdd = (Spinner) findViewById(R.id.countAdd);
        priceAdd = (EditText) findViewById(R.id.priceAdd);
        nameUpd = (Spinner) findViewById(R.id.nameUpd);
        countUpd = (Spinner) findViewById(R.id.countUpd);
        priceUpd = (EditText) findViewById(R.id.priceUpd);
        updData();
    }

    public void toStoreFront(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void add(View view) {
        if ((nameAdd.getText().toString().length() == 0)  || (priceAdd.getText().toString().length() == 0))
            Toast.makeText(this, "Для добавления товара нужно заполнить все поля.", Toast.LENGTH_SHORT).show();
        else {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            db.execSQL("INSERT INTO items ( name , count, price ) VALUES (\"" + nameAdd.getText().toString() + "\", " + Integer.parseInt(countAdd.getSelectedItem().toString()) + ", " + Double.parseDouble(priceAdd.getText().toString()) + " )");
            String message = "Товар \"" + nameAdd.getText() + "\"" + "добавлен.";
            nameAdd.setText(null);
            countAdd.setSelection(0);
            priceAdd.setText(null);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            db.close();
            updData();
        }
    }

    public void update(View view) {
        if (priceUpd.getText().toString().length() == 0)
            Toast.makeText(this, "Для обновления информации нужно заполнить все поля.", Toast.LENGTH_SHORT).show();
        else {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            db.execSQL("UPDATE items set count =" + Integer.parseInt(countUpd.getSelectedItem().toString()) + ", price = " + Double.parseDouble(priceUpd.getText().toString()) + " where name=\"" + nameUpd.getSelectedItem().toString() + "\"");
            String message = "Товар \"" + nameUpd.getSelectedItem().toString() + "\"" + "обновлен.";
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            nameUpd.setSelection(0);
            countUpd.setSelection(0);
            priceUpd.setText(null);
            db.close();
            updData();
        }
    }

    public void updData() {
        ArrayList<String> names = new ArrayList<String>();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("Select name from items", null);
        if (query.moveToFirst()) {
            do {
                names.add(query.getString(0));
            }
            while (query.moveToNext());
        }
        query.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameUpd.setAdapter(adapter);
    }
}
