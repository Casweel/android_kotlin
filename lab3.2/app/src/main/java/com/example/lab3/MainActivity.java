package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.System.runFinalizersOnExit;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private String DB_NAME;
    private int DB_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB_NAME = "app.db";
        db = getBaseContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
    }


    public void onMyButtonClick1(View view) {
        Intent intent = new Intent(this, ViewDB.class);
        startActivity(intent);
        finish();
    }

    public void onMyButtonClick2(View view) {
        db.execSQL("INSERT INTO students (name , dtime ) VALUES ('Антон gh h', DATETIME())");
    }

    public void onMyButtonClick3(View view) {
        db.execSQL("UPDATE students SET name='Иванов Иван Иванович' where (id = (SELECT max(id) from students))");
    }

    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    class DBUpdate extends SQLiteOpenHelper {
        public DBUpdate(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ArrayList<Students> students = new ArrayList<Students>();
            db.beginTransaction();
            Integer id;
            String[] fio;
            String dtime;

            try {
                Cursor query = db.rawQuery("SELECT * FROM students;", null);
                if (query.moveToFirst()) {
                    do {
                        students.add(new Students(query.getInt(0), query.getString(1), query.getString(2)));
                    }
                    while (query.moveToNext());
                }
                db.execSQL("DROP TABLE 'students'");
                db.execSQL("CREATE TABLE students (id INTEGER primary key autoincrement, familia TEXT, ima TEXT, otch TEXT, dtime TEXT)");
                for (int i = 0; i < students.size(); i++) {
                    id = students.get(i).getId();
                    fio = students.get(i).getFio().split(" ");
                    dtime = students.get(i).getDtime();
                    //f = pd.read_sql_query('SELECT open FROM NYSEMSFT WHERE date = (?)', conn, params=(date,))

                    db.execSQL("INSERT INTO students (id, familia, ima, otch , dtime ) VALUES (" + id.toString() + ", " + fio[0] + "," + fio[1] + "," + fio[2] + ", " + dtime + ")");

                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
