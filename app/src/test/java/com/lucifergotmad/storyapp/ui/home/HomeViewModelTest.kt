package com.lucifergotmad.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.lucifergotmad.storyapp.core.data.StoryRepository
import com.lucifergotmad.storyapp.core.domain.Story
import com.lucifergotmad.storyapp.core.preferences.SettingPreferences
import com.lucifergotmad.storyapp.core.preferences.UserPreferences
import com.lucifergotmad.storyapp.mock.DataDummy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var settingPreferences: SettingPreferences

    private val dataDummy = DataDummy.generateDummyStories();

    @Before
    fun setUp() {
        viewModel = HomeViewModel(storyRepository, userPreferences, settingPreferences)
    }

    @Test
    fun `when getStories should not null`() {
        val observer = Observer<PagingData<Story>> {}
        val fakeToken = "fakeToken"
        val expected = listOf<Story>(
            Story("1", "Octyo Paswa Putra", "Fullstack Developer", "notFound", null, null),
            Story("2", "Rani Riana", "Helper", "notFound", null, null)
        )
        try {

            `when`(storyRepository.getStoriesPaging(fakeToken))
                .thenReturn(liveData { emit(PagingData.from(dataDummy)) })

            val actualStories = viewModel.getStoriesPaging(fakeToken).observeForever(observer)

            verify(storyRepository).getStoriesPaging(fakeToken)

            Assert.assertNotNull(actualStories)
            Assert.assertEquals(dataDummy, actualStories)
        } finally {
            viewModel.getStoriesPaging(fakeToken).removeObserver(observer)
        }

    }
}