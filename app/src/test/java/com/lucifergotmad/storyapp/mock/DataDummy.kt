package com.lucifergotmad.storyapp.mock

import com.lucifergotmad.storyapp.core.domain.Story

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val stories = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                id = i.toString(),
                name = "Name $i",
                description = "Description $i",
                photoUrl = "Photo Url $i",
                null,
                null,
            )

            stories.add(story)
        }

        return stories
    }
}