package com.yourbynn.app_githubusers.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory
import com.yourbynn.app_githubusers.R
import com.yourbynn.app_githubusers.databinding.ActivitySettingBinding
import com.yourbynn.app_githubusers.pref.SettingViewModelFactory
import com.yourbynn.app_githubusers.pref.SettingsTheme
import com.yourbynn.app_githubusers.pref.SettingsViewModel
import com.yourbynn.app_githubusers.pref.dataStore

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsTheme.getInstance(application.dataStore)
        settingsViewModel = ViewModelProvider(this@SettingActivity, SettingViewModelFactory(pref))[SettingsViewModel::class.java]

        settingsViewModel.getTheme().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnDarkMode.isChecked = false
            }
        }

        binding.btnDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingsViewModel.saveTheme(isChecked)
        }
    }
}