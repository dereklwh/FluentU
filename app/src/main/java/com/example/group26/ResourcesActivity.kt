package com.example.group26

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ResourcesActivity:AppCompatActivity() {

    private lateinit var resourceButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        resourceButton = findViewById(R.id.resource_Button)
        resourceButton.setOnClickListener{
            finish()
        }
    }
}