package com.example.group26

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class QuizActivity: AppCompatActivity() {
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        submitButton = findViewById(R.id.submitButton)
        submitButton.setOnClickListener{
            finish()
        }
    }
}