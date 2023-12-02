package com.example.group26.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.group26.R

class DailyTipWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    //Only get a tip if there isn't one already
    override fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences(
            "DailyTipPrefs",
            Context.MODE_PRIVATE
        )
        if (!sharedPreferences.contains("daily_tip")) {
            val tipsArray = applicationContext.resources.getStringArray(R.array.language_tips)
            displayRandomTip(tipsArray, sharedPreferences)
        }
        return Result.success()
    }
    //Put String in shared preferences to be used in Main activity
    private fun displayRandomTip(tipsArray: Array<String>, sharedPreferences: SharedPreferences) {
        val random = (tipsArray.indices).random()
        val randomTip = tipsArray[random]
        sharedPreferences.edit().putString("daily_tip", randomTip).apply()
    }
}