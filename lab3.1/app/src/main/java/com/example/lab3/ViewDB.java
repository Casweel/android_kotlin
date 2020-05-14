package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_d_b);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        TextView textView = findViewById(R.id.textView);
        textView.append("Students\n\n");
        Cursor query = db.rawQuery("SELECT * FROM students;", null);
        if(query.moveToFirst()){
            do{
                int id = query.getInt(0);
                String name = query.getString(1);
                String time = query.getString(2);
                textView.append("ID: " + id + " Name: " + name + " Time: " + time + "\n\n");
            }
            while(query.moveToNext());
        }
        query.close();
        db.close();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent  = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
