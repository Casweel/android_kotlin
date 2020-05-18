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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        if ((nameAdd.getText().toString().length() == 0) || (priceAdd.getText().toString().length() == 0)) {
            Toast.makeText(this, "Для добавления товара нужно заполнить все поля.", Toast.LENGTH_SHORT).show();
        } else {
            String name = nameAdd.getText().toString();
            Integer count = Integer.parseInt(countAdd.getSelectedItem().toString());
            Double price = Double.parseDouble(priceAdd.getText().toString());

            Params params = new Params(this, getBaseContext(), name, count, price, nameUpd);
            Add asyncTask = new Add();
            asyncTask.execute(params);
            nameAdd.setText(null);
            countAdd.setSelection(0);
            priceAdd.setText(null);
        }

    }

    public void update(View view) {
        if (priceUpd.getText().toString().length() == 0) {
            Toast.makeText(this, "Для добавления товара нужно заполнить все поля.", Toast.LENGTH_SHORT).show();
        } else {
            String name = nameUpd.getSelectedItem().toString();
            Integer count = Integer.parseInt(countUpd.getSelectedItem().toString());
            Double price = Double.parseDouble(priceUpd.getText().toString());

            Params params = new Params(this, getBaseContext(), name, count, price, nameUpd);
            Update asyncTask = new Update();
            asyncTask.execute(params);
            nameUpd.setSelection(0);
            countUpd.setSelection(0);
            priceUpd.setText(null);
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
