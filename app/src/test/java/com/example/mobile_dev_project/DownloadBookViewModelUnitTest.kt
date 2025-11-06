package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.ui.model.ImportPhase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

/**
 * unit tests for DownloadBookViewModel
 * - verifies input validation, progress pipeline, err propagation w coroutine based ui
 * - use kotlinx coroutines test StandardTestDispatcher and virtual time control
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DownloadBookViewModelUnitTest {
    private lateinit var fakeImporter: FakeBookImporter
    private lateinit var viewModel: DownloadBookViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeImporter = FakeBookImporter()
        viewModel = DownloadBookViewModel(fakeImporter, null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_onUrlChanged_updatesStateAndClearsErr() {
        // simulate previous error by entering a valid url
        viewModel.onUrlChanged("fail")
        viewModel.onUrlChanged("https://test.com")
        assertEquals("https://test.com", viewModel.url)
        assertNull(viewModel.errorText)
    }

    @Test
    fun test_invalid_url_sets_errText() = runTest {
        viewModel.onUrlChanged("not_a_url")
        viewModel.onAddClicked()
        assertEquals("Please enter a valid URL (http/https)", viewModel.errorText)
    }

    @Test
    fun test_valid_url_triggers_import_and_progress() = runTest {
        viewModel.onUrlChanged("https://good.com")
        viewModel.onAddClicked()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(fakeImporter.importCalledWith?.isNotEmpty() == true)
        assertNotNull(viewModel.progress)
        // Only the final state is visible after all coroutines
        assertEquals(ImportPhase.DONE, viewModel.progress?.phase)
    }

    @Test
    fun test_import_setsIsImportingDuringPipeline() = runTest {
        viewModel.onUrlChanged("https://good.com")
        viewModel.onAddClicked()
        testDispatcher.scheduler.advanceUntilIdle()
        // this should be false at end of pipeline
        assertFalse(viewModel.isImporting)
    }

    @Test
    fun test_onAddClicked_handlesFailure() = runTest {
        fakeImporter.shouldFail = true
        viewModel.onUrlChanged("https://fail.com")
        viewModel.onAddClicked()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.errorText)
    }
}
