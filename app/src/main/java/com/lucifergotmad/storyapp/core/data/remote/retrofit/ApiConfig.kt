package com.lucifergotmad.storyapp.core.data.remote.retrofit

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {


    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
        private fun setupRetrofit(): Retrofit {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
        }

        fun getStoryService(): StoryService {
            return setupRetrofit().create(StoryService::class.java)
        }

        fun getUserService(): UserService {
            return setupRetrofit().create(UserService::class.java)
        }
    }
}