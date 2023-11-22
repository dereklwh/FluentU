package com.example.group26.ui.Profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)

    fun saveProfileData(name: String, email: String, phone: String, gender: String) {
        sharedPreferences.edit().apply {
            putString("NAME", name)
            putString("EMAIL", email)
            putString("PHONE", phone)
            putString("GENDER", gender)
            apply()
        }
    }

    fun getSavedProfileData(): Triple<String, String, String> {
        val name = sharedPreferences.getString("NAME", "") ?: ""
        val email = sharedPreferences.getString("EMAIL", "") ?: ""
        val phone = sharedPreferences.getString("PHONE", "") ?: ""
        return Triple(name, email, phone)
    }

    fun getSavedGender(): String {
        return sharedPreferences.getString("GENDER", "") ?: ""
    }
}