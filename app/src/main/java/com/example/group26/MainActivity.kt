package com.example.group26

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.group26.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var imageButtons: Array<ImageButton>
    private lateinit var binding: ActivityMainBinding

    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    fun setupButtons(){
        imageButtons = arrayOf(
            findViewById(R.id.quizButton),
            findViewById(R.id.flashCardButton),
            findViewById(R.id.progressButton),
            findViewById(R.id.resourcesButton)
        )

        for(btn in imageButtons){
            btn.setOnClickListener(){
                val intent:Intent;
                if(btn == imageButtons[0]){
                    intent = Intent(this, QuizActivity::class.java)
                }
                else if(btn == imageButtons[1]){
                    intent = Intent(this, FlashCardActivity::class.java)
                }
                else if(btn == imageButtons[2]){
                    intent = Intent(this, ProgressActivity::class.java)
                }
                else{
                    intent = Intent(this, ResourcesActivity::class.java)
                }
                startActivity(intent)
            }
        }
    }
}