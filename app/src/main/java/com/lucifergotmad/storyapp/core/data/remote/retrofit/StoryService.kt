package com.lucifergotmad.storyapp.core.data.remote.retrofit

import com.lucifergotmad.storyapp.core.data.remote.response.DetailStoryResponse
import com.lucifergotmad.storyapp.core.data.remote.response.PostResponse
import com.lucifergotmad.storyapp.core.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") authToken: String
    ): PostResponse

    @GET("stories")
    suspend fun getALlStories(
        @Header("Authorization") authToken: String,
        @Query("location") location: Int?
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") authToken: String
    ): DetailStoryResponse
}