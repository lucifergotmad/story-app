package com.lucifergotmad.storyapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.UserPreferences

class DetailStoryViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

    fun getStoryDetail(id: String, token: String) = storyRepository.getStoryDetail(id, token)

}