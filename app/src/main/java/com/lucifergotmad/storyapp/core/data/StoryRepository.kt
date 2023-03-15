package com.lucifergotmad.storyapp.core.data

import com.lucifergotmad.storyapp.core.data.remote.retrofit.StoryService
import com.lucifergotmad.storyapp.core.data.remote.retrofit.UserService

class StoryRepository(
    private val mStoryService: StoryService,
    private val mUserService: UserService
) {


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyService: StoryService,
            userService: UserService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyService, userService)
            }.also { instance = it }
    }
}