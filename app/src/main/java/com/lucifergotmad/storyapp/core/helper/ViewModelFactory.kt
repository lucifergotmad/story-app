package com.lucifergotmad.storyapp.core.helper

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.di.Injection

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(""),
                )
            }.also { instance = it }
    }
}