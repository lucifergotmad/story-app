package com.lucifergotmad.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.preferences.UserPreferences

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    fun getUser(): LiveData<String> {
        return userPreferences.getUserToken().asLiveData()
    }
}