package com.lucifergotmad.storyapp.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.lucifergotmad.storyapp.core.data.remote.request.LoginUserRequest
import com.lucifergotmad.storyapp.core.data.remote.request.RegisterUserRequest
import com.lucifergotmad.storyapp.core.data.remote.response.PostLoginResponse
import com.lucifergotmad.storyapp.core.data.remote.response.PostResponse
import com.lucifergotmad.storyapp.core.data.remote.retrofit.StoryService
import com.lucifergotmad.storyapp.core.data.remote.retrofit.UserService
import com.lucifergotmad.storyapp.core.domain.DetailStory
import com.lucifergotmad.storyapp.core.domain.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val mStoryService: StoryService, private val mUserService: UserService
) {

    fun registerUser(data: RegisterUserRequest): LiveData<Result<PostResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = mUserService.register(data)
            if (response.error) {
                emit(Result.Error(response.message))
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
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
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(token: String): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val response = mStoryService.getALlStories(token)
            if (response.error == true) {
                emit(Result.Error(response.message ?: ""))
            }

            val listStories = response.listStory.map {
                Story(it.id, it.name, it.description, it.photoUrl)
            }

            emit(Result.Success(listStories))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryDetail(id: String, token: String): LiveData<Result<DetailStory>> = liveData {
        emit(Result.Loading)
        try {
            val response = mStoryService.getDetailStory(id, token)
            if (response.error) {
                emit(Result.Error(response.message))
            }

            val detailStory = response.story.let {
                DetailStory(it.id, it.name, it.description, it.photoUrl, it.createdAt)
            }

            emit(Result.Success(detailStory))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String
    ): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = mStoryService.addStory(file, description, token)
            if (response.error) {
                emit(Result.Error(response.message))
            }

            emit(Result.Success(response.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        private const val TAG = "StoryRepository"
        fun getInstance(
            storyService: StoryService, userService: UserService
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(storyService, userService)
        }.also { instance = it }
    }
}