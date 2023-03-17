package com.lucifergotmad.storyapp.core.data.remote.retrofit

import com.lucifergotmad.storyapp.core.data.remote.request.LoginUserRequest
import com.lucifergotmad.storyapp.core.data.remote.request.RegisterUserRequest
import com.lucifergotmad.storyapp.core.data.remote.response.PostLoginResponse
import com.lucifergotmad.storyapp.core.data.remote.response.PostResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun register(@Body data: RegisterUserRequest): PostResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body data: LoginUserRequest): PostLoginResponse

}