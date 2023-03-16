package com.lucifergotmad.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.data.remote.request.RegisterUserRequest

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun register(data: RegisterUserRequest) = storyRepository.registerUser(data)

}