package com.lucifergotmad.storyapp.core.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.lucifergotmad.storyapp.core.data.remote.request.LoginUserRequest
import com.lucifergotmad.storyapp.core.data.remote.request.RegisterUserRequest
import com.lucifergotmad.storyapp.core.data.remote.response.PostLoginResponse
import com.lucifergotmad.storyapp.core.data.remote.response.PostResponse
import com.lucifergotmad.storyapp.core.data.remote.retrofit.StoryService
import com.lucifergotmad.storyapp.core.data.remote.retrofit.UserService

class StoryRepository(
    private val mStoryService: StoryService,
    private val mUserService: UserService
) {

    fun registerUser(data: RegisterUserRequest): LiveData<Result<PostResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = mUserService.register(data)
            Log.d(TAG, "response: $response")
            if (response.error) {
                emit(Result.Error(response.message))
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "registerUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun loginUser(data: LoginUserRequest): LiveData<Result<PostLoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = mUserService.login(data)
            if (response.error) {
                emit(Result.Error(response.message))
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(TAG, "loginUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        private const val TAG = "StoryRepository"
        fun getInstance(
            storyService: StoryService,
            userService: UserService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyService, userService)
            }.also { instance = it }
    }
}