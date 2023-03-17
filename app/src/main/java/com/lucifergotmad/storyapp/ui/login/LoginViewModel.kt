package com.lucifergotmad.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.data.remote.request.LoginUserRequest
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun loginUser(data: LoginUserRequest) = storyRepository.loginUser(data)

    fun saveUser(user: User) = viewModelScope.launch {
        userPreferences.saveUser(user)
    }

}