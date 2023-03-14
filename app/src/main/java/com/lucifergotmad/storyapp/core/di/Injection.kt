package com.lucifergotmad.storyapp.core.di

import android.content.Context
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(token: String): StoryRepository {
        val storyService = ApiConfig.getStoryService(token)
        val userService = ApiConfig.getUserService()
        return StoryRepository.getInstance(storyService, userService)
    }
}