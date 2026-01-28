package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.mobile_dev_project.ui.model.ImportPhase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mobile_dev_project.data.importer.BookImporterContract
import kotlinx.coroutines.delay

/**
 * Compose ui test for DownloadBookScreen
 * test the DownloadBookScreen Composable directly, with local test ViewModel
 */
class DownloadBookScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var testViewModel: DownloadBookViewModel

    @Before
    fun setup() {
        // provide minimal fake Importer for the ViewModel
        val fakeImporter = object : BookImporterContract {
            override suspend fun importBooks(sources: List<Pair<String, String>>, onProgress: (com.example.mobile_dev_project.ui.model.ProgressState) -> Unit) {
                onProgress(com.example.mobile_dev_project.ui.model.ProgressState(ImportPhase.DOWNLOADING, "Starting download..."))
                delay(500)
                onProgress(com.example.mobile_dev_project.ui.model.ProgressState(ImportPhase.DONE, "Done!"))
            }
        }
        testViewModel = DownloadBookViewModel(fakeImporter, null)
        //Set content before every test to get vm
        composeTestRule.setContent {
            DownloadBookScreen(
                onBack = {},
                vm = testViewModel
            )
        }
    }

    @Test
    fun urlField_acceptsInput_and_enablesAddButton() {
        composeTestRule.onNodeWithTag("UrlField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AddButton").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("UrlField").performTextInput("https://www.gutenberg.org/cache/epub/25344/pg25344-h.zip")
        composeTestRule.onNodeWithTag("AddButton").assertIsEnabled()
    }

    @Test
    fun entering_invalidUrl_showsErrorText() {
        composeTestRule.onNodeWithTag("UrlField").performTextInput("not_a_url")
        composeTestRule.onNodeWithTag("AddButton").performClick()
        composeTestRule.onNodeWithText("Please enter a valid URL (http/https)").assertIsDisplayed()
    }

    @Test
    fun clickingCancelButton_isDisplayedAndClickable() {
        composeTestRule.onNodeWithTag("CancelButton").assertExists()
        composeTestRule.onNodeWithTag("CancelButton").assertIsEnabled()
        composeTestRule.onNodeWithTag("CancelButton").performClick()
    }

    @Test
    fun import_showsDoneButtonAppears() {
        composeTestRule.onNodeWithTag("UrlField").performTextClearance()
        composeTestRule.onNodeWithTag("UrlField").performTextInput("https://www.gutenberg.org/cache/epub/145/pg145-h.zip")
        composeTestRule.onNodeWithTag("AddButton").performClick()
        // wait for Done button to appear
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithTag("DoneButton").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("DoneButton").assertIsDisplayed()
    }
}
