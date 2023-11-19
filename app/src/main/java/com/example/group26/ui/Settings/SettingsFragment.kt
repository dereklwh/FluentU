package com.example.group26.ui.Settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.group26.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}