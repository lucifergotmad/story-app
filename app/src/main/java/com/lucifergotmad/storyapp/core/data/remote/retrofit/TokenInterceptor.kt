package com.lucifergotmad.storyapp.core.data.remote.retrofit

import okhttp3.Interceptor

class TokenInterceptor(private val tokenType: String, private val accessToken: String?) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $accessToken").build()

        return chain.proceed(request)
    }
}