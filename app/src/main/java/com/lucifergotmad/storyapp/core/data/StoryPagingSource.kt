package com.lucifergotmad.storyapp.core.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lucifergotmad.storyapp.core.data.remote.retrofit.StoryService
import com.lucifergotmad.storyapp.core.domain.Story

class StoryPagingSource(private val apiService: StoryService, private val token: String) :
    PagingSource<Int, Story>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesPaging(token, position, params.loadSize)
            val stories = responseData.listStory.map {
                Story(
                    it.id,
                    it.name,
                    it.description,
                    it.photoUrl,
                    it.lat,
                    it.lon
                )
            }

            Log.d("StoryPagingSource", "${stories.size}")

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}