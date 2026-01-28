package com.example.mobile_dev_project

import com.example.mobile_dev_project.data.SearchResult
import com.example.mobile_dev_project.data.repository.SearchRepository
import com.example.mobile_dev_project.ui.screens.SearchViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 *unit tests for SearchViewModel
 * this holds the query, results, and has isSearching & eerMsg
 *when onQueryChanged() is called, it, updates query, and then trims it
 *if blank, clear results & return
 * otherise,launch coroutine in viewModelScope & call searchRepository.search(trimmed)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var searchRepository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        searchRepository = mockk()
        viewModel = SearchViewModel(searchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun blankQuery_noRepoCall() = runTest {
        // type space
        viewModel.onQueryChanged("   ")
        advanceUntilIdle()

        coVerify(exactly = 0) { searchRepository.search(any()) }

        //results should be empty, no err msg, not in loadinf state
        assertTrue(viewModel.results.isEmpty())
        assertEquals(null, viewModel.errMsg)
        assertEquals(false, viewModel.isSearching)
    }

    @Test
    fun normalQuery_updatesResults() = runTest {
        //building fake results that repo return
        val fakeResults = listOf(
            SearchResult(
                bookId = 1,
                chapterId = 10,
                contentId = 100,
                chapterTitle = "Chapter 1 - The Aeroplane",
                query = "boy",
                snippet = "The boy was delighted with the prospect of seeing the great scientist Tesla...",
                scrollRatio = 0.2f
            ),
            SearchResult(
                bookId = 1,
                chapterId = 11,
                contentId = 101,
                chapterTitle = "Chapter 2 - Aeroplane Development",
                query = "boy",
                snippet = "Tesla's plan offers a field for limitless study and discussion, but to the boy who is interested in electricity it offers one of the most fascinating subjects for reading and thinking in all the realm of science...",
                scrollRatio = 0.8f
            )
        )

        coEvery { searchRepository.search("boy") } returns fakeResults

        //type w spaces
        val typed = "  boy  "
        viewModel.onQueryChanged(typed)
        advanceUntilIdle()

        //repo is called once w the trimmed query boy
        coVerify(exactly = 1) { searchRepository.search("boy") }

        assertEquals(typed, viewModel.query)
        assertEquals(fakeResults, viewModel.results)
        assertEquals(null, viewModel.errMsg)
        assertEquals(false, viewModel.isSearching)
    }

    @Test
    fun repoError_setsErrMsgAndClearsResults() = runTest {
        // repo throw exception when searching for "tesla"
        val errorMessage = "db erro"
        coEvery { searchRepository.search("tesla") } throws RuntimeException(errorMessage)

        viewModel.onQueryChanged("tesla")
        advanceUntilIdle()

        coVerify(exactly = 1) { searchRepository.search("tesla") }

        assertTrue(viewModel.results.isEmpty())
        assertEquals(errorMessage, viewModel.errMsg)
        assertEquals(false, viewModel.isSearching)
    }
}
