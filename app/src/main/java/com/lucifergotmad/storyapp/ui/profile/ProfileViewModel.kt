package com.lucifergotmad.storyapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.SettingPreferences
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val userPreferences: UserPreferences,
    private val settingPreferences: SettingPreferences
) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun deleteUser() = viewModelScope.launch {
        userPreferences.deleteUser()
    }

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

}