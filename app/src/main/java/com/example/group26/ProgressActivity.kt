package com.example.group26

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProgressActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_progress)

        val close: Button = findViewById(R.id.progressCloseButton)

        close.setOnClickListener(){
            finish()
        }
    }
}