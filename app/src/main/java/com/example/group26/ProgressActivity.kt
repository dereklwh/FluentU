package com.example.group26

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.group26.database.AppDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class ProgressActivity : AppCompatActivity() {
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_progress)

        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)
        imageView4 = findViewById(R.id.imageView4)
        calculateStreak()

        val barChart: BarChart = findViewById(R.id.barChart)

        // retrieve progress data from the database
        val progressDao = AppDatabase.getDatabase(applicationContext).progressTrackerDao()
        val allProgressLiveData = progressDao.getAllProgress()

        allProgressLiveData.observe(this, Observer { allProgress ->
            // convert ProgressData to BarEntries
            val barEntries = allProgress.mapIndexed { index, progressData ->
                BarEntry((index + 1).toFloat(), progressData.score.toFloat())
            }

	    // create the data
            val barDataSet = BarDataSet(barEntries, "Score vs Attempts")
            val barData = BarData(barDataSet)

            // set data to the chart
            barChart.data = barData

            // change the x-axis to show integers
            val xAxis: XAxis = barChart.xAxis
            xAxis.granularity = 1f 
            xAxis.valueFormatter = IntAxisValueFormatter() 
	    
	        // change the y-axis to show integers and hide the right y-axis
            val yAxis: YAxis = barChart.axisLeft
            yAxis.granularity = 1f
            yAxis.axisMinimum = 0f
            barChart.axisRight.isEnabled = false

            barChart.setDrawValueAboveBar(true)
            barChart.description.isEnabled = false
            barChart.xAxis.labelRotationAngle = -45f

            barChart.invalidate()
        })
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateStreak() {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastSubmissionDate = sharedPreferences.getString("last_submission_date", "") ?: ""

        if (lastSubmissionDate.isNotEmpty()) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val streak = calculateDateStreak(lastSubmissionDate, currentDate)

            updateUIBasedOnStreak(streak)
        }
    }

    // calculates the number of days between the most recent submission
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDateStreak(lastDate: String, currentDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val lastDateTime = LocalDate.parse(lastDate, formatter)
        val currentDateTime = LocalDate.parse(currentDate, formatter)

        return ChronoUnit.DAYS.between(lastDateTime, currentDateTime).toInt()
    }

    private fun updateUIBasedOnStreak(streak: Int) {
        if (streak >= 7) {
            // change color for 7-day streak
            imageView2.setColorFilter(Color.GREEN)
            imageView3.setColorFilter(Color.GREEN)
            imageView4.setColorFilter(Color.GREEN)
        } else if (streak >= 5) {
            // change color for 5-day streak
            imageView2.setColorFilter(Color.YELLOW)
            imageView3.setColorFilter(Color.YELLOW)
            imageView4.setColorFilter(Color.YELLOW)
        } else if (streak >= 3) {
            // change color for 3-day streak
            imageView2.setColorFilter(Color.RED)
            imageView3.setColorFilter(Color.RED)
            imageView4.setColorFilter(Color.RED)
        } else {
            // reset color if the streak is less than 3 days
            imageView2.clearColorFilter()
            imageView3.clearColorFilter()
            imageView4.clearColorFilter()
        }
    }
}