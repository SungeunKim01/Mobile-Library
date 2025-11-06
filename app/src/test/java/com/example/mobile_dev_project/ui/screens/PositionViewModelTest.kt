package com.example.mobile_dev_project.ui.screens


import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PositionViewModelTest {
    //https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/kotlinx.coroutines.test/-standard-test-dispatcher.html
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: ContentRepository
    private lateinit var viewModel: PositionViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val mockDao = mock<ContentDao>()
        fakeRepository = FakeContentRepository(mockDao)
        viewModel = PositionViewModel(fakeRepository)
    }
    @After
    fun tear_down() {
        Dispatchers.resetMain()
    }

    @Test
    fun getScrollPosition_ReturnsCorrectValue() = runTest {
        val contentId = 1
        val pos = 0.75f

        fakeRepository.updateScreenPosition(contentId, pos)

        val result = viewModel.getScrollPosition(contentId)

        assertEquals(pos, result)

    }
    //https://developer.android.com/kotlin/coroutines/test
    @Test
    fun saveScrollPosition_StoresDatabaseOnce() = runTest{
        val contentId = 2
        val pos = 0.5f

        viewModel.saveScrollPosition(contentId, pos)
        advanceUntilIdle()
        assertEquals(pos, fakeRepository.getScreenPosition(contentId))
    }
}