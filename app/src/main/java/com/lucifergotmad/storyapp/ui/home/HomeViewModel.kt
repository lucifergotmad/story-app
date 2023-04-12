package com.lucifergotmad.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.SettingPreferences
import com.lucifergotmad.storyapp.core.preferences.UserPreferences

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences,
    private val settingPreferences: SettingPreferences
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

    fun getStories(token: String) = storyRepository.getStories(token, null)

    fun getStoriesPaging(token: String) = storyRepository.getStoriesPaging(token).cachedIn(viewModelScope)
}