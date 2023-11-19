package com.yourbynn.app_githubusers.pref

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yourbynn.app_githubusers.model.MainViewModel

class SettingViewModelFactory(private val preferences: SettingsTheme) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(preferences) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}