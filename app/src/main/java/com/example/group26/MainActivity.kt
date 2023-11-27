package com.example.group26

import android.content.Intent
import android.os.Bundle
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.group26.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar


private const val PREFERENCES_NOTIFICATION_KEY = "push_notification"

class MainActivity : AppCompatActivity() {
    private lateinit var imageButtons: Array<ImageButton>
    private lateinit var binding: ActivityMainBinding

    private val api: String = "AIzaSyC0LA82UScnqYhuh-e_urF_aH7h_CZ-y7A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scheduleNotifications()

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

    /*
        Schedule Notifications for 12pm daily
    */
    private fun scheduleNotifications() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationsEnabled = sharedPreferences.getBoolean(PREFERENCES_NOTIFICATION_KEY, true)

        if(notificationsEnabled) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(this, NotificationReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

            // Set the hour to 12 PM t
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            // Check if the time has already passed for today; If so just send the notification
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                val intent = Intent(this, NotificationReceiver::class.java)
                sendBroadcast(intent)
            }

            // Set the alarm to trigger daily at 12 PM
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }else Toast.makeText(this, "Notification's are disabled.", Toast.LENGTH_SHORT).show()
    }
}