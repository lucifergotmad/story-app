package com.lucifergotmad.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

    fun addStory(file: MultipartBody.Part, description: RequestBody, token: String) =
        storyRepository.addStory(file, description, token)

}