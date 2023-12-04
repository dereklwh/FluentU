package com.example.group26

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.group26.database.AppDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_progress)

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
}