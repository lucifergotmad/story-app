package com.lucifergotmad.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.lucifergotmad.storyapp.core.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesLocation(token: String, location: String) =
        storyRepository.getStories(token, location)

}