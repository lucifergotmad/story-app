package com.lucifergotmad.storyapp.core.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.di.Injection
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import com.lucifergotmad.storyapp.ui.home.HomeViewModel
import com.lucifergotmad.storyapp.ui.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(
            dataStore: DataStore<Preferences>,
            token: String?
        ): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(token),
                    Injection.providePreferences(dataStore),
                )
            }.also { instance = it }
    }
}