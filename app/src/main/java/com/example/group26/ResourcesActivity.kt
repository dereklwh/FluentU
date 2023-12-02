package com.example.group26

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ResourcesActivity:AppCompatActivity() {

    private lateinit var resourcesTitle: TextView
    private lateinit var etcTitle: TextView
    private lateinit var aboutTitle: TextView

    private lateinit var resourcesContent: TextView
    private lateinit var etcContent: TextView
    private lateinit var aboutContent: TextView
    private lateinit var resourceButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        resourcesTitle = findViewById(R.id.appName)
        etcTitle = findViewById(R.id.etcTitle)
        aboutTitle = findViewById(R.id.aboutTitle)

        resourcesContent = findViewById(R.id.resourceText)
        etcContent = findViewById(R.id.ectText)
        aboutContent = findViewById(R.id.aboutText)


        // toggles the visibility of the text
        resourcesTitle.setOnClickListener {
            if (resourcesContent.visibility == View.VISIBLE) {
                resourcesContent.visibility = View.GONE
            } else {
                resourcesContent.visibility = View.VISIBLE
            }
        }

        etcTitle.setOnClickListener {
            if (etcContent.visibility == View.VISIBLE) {
                etcContent.visibility = View.GONE
            } else {
                etcContent.visibility = View.VISIBLE
            }
        }

        aboutTitle.setOnClickListener {
            if(aboutContent.visibility == View.VISIBLE) {
                aboutContent.visibility = View.GONE
            } else {
                aboutContent.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return true
    }
}