package com.lucifergotmad.storyapp.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailStory(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String
) : Parcelable