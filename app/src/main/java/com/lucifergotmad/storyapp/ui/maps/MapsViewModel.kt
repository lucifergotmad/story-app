package com.lucifergotmad.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.UserPreferences

class MapsViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getStoriesLocation(token: String) =
        storyRepository.getStories(token, 1)

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }
}