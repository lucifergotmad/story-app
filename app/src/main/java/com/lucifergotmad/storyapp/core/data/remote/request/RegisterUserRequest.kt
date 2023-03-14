package com.lucifergotmad.storyapp.core.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest (
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)