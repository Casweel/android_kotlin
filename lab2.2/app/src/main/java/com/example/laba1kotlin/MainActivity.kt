package com.example.laba1kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

    override fun onResume() {
        super.onResume()
        Thread {
            Thread.sleep(100)
            val secondSlideIntent = Intent (this, SecondActivity::class.java)
            startActivity(secondSlideIntent)
            finish()
        }.start()
    }
}
