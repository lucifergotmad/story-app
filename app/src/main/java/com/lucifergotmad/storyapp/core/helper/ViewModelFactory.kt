package com.lucifergotmad.storyapp.core.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.di.Injection
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import com.lucifergotmad.storyapp.ui.add.AddStoryViewModel
import com.lucifergotmad.storyapp.ui.detail.DetailStoryViewModel
import com.lucifergotmad.storyapp.ui.home.HomeViewModel
import com.lucifergotmad.storyapp.ui.login.LoginViewModel
import com.lucifergotmad.storyapp.ui.profile.ProfileViewModel
import com.lucifergotmad.storyapp.ui.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository, userPreferences) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository, userPreferences) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(storyRepository, userPreferences) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository, userPreferences) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(
            dataStore: DataStore<Preferences>,
        ): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(),
                    Injection.providePreferences(dataStore),
                )
            }.also { instance = it }
    }
}