package com.lucifergotmad.storyapp.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.data.remote.retrofit.ApiConfig
import com.lucifergotmad.storyapp.core.preferences.SettingPreferences
import com.lucifergotmad.storyapp.core.preferences.UserPreferences

object Injection {
    fun provideRepository(): StoryRepository {
        val storyService = ApiConfig.getStoryService()
        val userService = ApiConfig.getUserService()
        return StoryRepository.getInstance(storyService, userService)
    }

    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences {
        return UserPreferences.getInstance(dataStore)
    }

    fun provideSettingPreferences(dataStore: DataStore<Preferences>): SettingPreferences {
        return SettingPreferences.getInstance(dataStore)
    }
}