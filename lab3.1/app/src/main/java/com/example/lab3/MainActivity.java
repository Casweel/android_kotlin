package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import static java.lang.System.runFinalizersOnExit;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS students (id INTEGER primary key autoincrement, name TEXT, dtime TEXT)");
        db.delete("students", null, null);
        db.execSQL("INSERT INTO students ( name , dtime ) VALUES ('Андрей b h', DATETIME())");
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Аня h jlk', DATETIME())");
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Толя g gh', DATETIME())");
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Коля cgh hjh', DATETIME())");
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Степан ghg y', DATETIME())");

    }

    public void onMyButtonClick1(View view)
    {
        Intent intent  = new Intent(this, ViewDB.class);
        startActivity(intent);
        finish();
    }
    public void onMyButtonClick2(View view)
    {
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Антон', DATETIME())");
    }
    public void onMyButtonClick3(View view)
    {
        db.execSQL("UPDATE students SET name='Иванов Иван Иванович' where (id = (SELECT max(id) from students))");
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
