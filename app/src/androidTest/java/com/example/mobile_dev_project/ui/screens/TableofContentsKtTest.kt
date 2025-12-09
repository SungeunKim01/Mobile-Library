package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.data.UiChapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TableOfContentsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeChapters = listOf(
        UiChapter(1, "Chapter 1", 1, 1, 1),
        UiChapter(2, "Chapter 2", 2, 1, 1),
        UiChapter(3, "Chapter 3", 3, 1, 1)
    )

    // BASIC UI TESTS

    // Test 1: Verify that the title "Table of Contents" is visible on the screen
    @Test
    fun title_isDisplayed() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("toc_title", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Table of Contents")
    }

    // Test 2: Ensure that all chapter titles appear on screen
    @Test
    fun chapters_areDisplayed() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        fakeChapters.forEach { chapter ->
            composeTestRule.onNodeWithText(chapter.chapterTitle!!, useUnmergedTree = true)
                .assertIsDisplayed()
        }
    }

    // Test 3: Confirm that clicking a chapter calls the callback function
    @Test
    fun clickingChapter_callsCallback() {
        var selectedChapter: String? = null
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onChapterSelected = { selectedChapter = it.chapterTitle },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Chapter 1", useUnmergedTree = true)
            .performClick()

        assert(selectedChapter == "Chapter 1")
    }

    // Test 4: Check that clicking the background toggles fullscreen text
    @Test
    fun clickingBackground_togglesFullscreenText() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("fullscreen_text", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()
    }

    // Test 5: Check that the title is not inside the LazyColumn list
    @Test
    fun title_isInsideTopColumn() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("toc_title", useUnmergedTree = true)
            .assertExists()
            .assert(hasParent(hasTestTag("toc_list").not()))
    }

    // Test 6: Verify that each chapter button is a child of the LazyColumn
    @Test
    fun chapterButtons_areInsideLazyColumn() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        fakeChapters.forEach { chapter ->
            composeTestRule.onNodeWithTag("chapter_button_${chapter.chapterId}", useUnmergedTree = true)
                .assertExists()
                .assert(hasParent(hasTestTag("toc_list")))
        }
    }

    // Test 7: Check that the floating back button exists and is inside the main Box
    @Test
    fun backButton_isInsideBoxAndVisible() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("back_button", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assert(hasParent(hasTestTag("toc_box")))
    }

    // Test 8: Confirm that the fullscreen text is inside the Box when active
    @Test
    fun fullscreenText_isInsideBox_whenVisible() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("fullscreen_text", useUnmergedTree = true)
            .assertExists()
            .assert(hasParent(hasTestTag("toc_box")))
    }

    // Test 9: If no chapters are passed, the LazyColumn should exist but be empty
    @Test
    fun noChapters_showsEmptyList() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = emptyList())

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithTag("toc_list", useUnmergedTree = true)
            .assertExists()
        composeTestRule.onAllNodesWithText("Chapter", useUnmergedTree = true)
            .assertCountEquals(0)
    }

    // Test 10: Verify that clicking multiple times toggles fullscreen correctly
    @Test
    fun multipleClicks_toggleFullscreenRepeatedly() {
        val fakeViewModel = FakeRetrieveDataViewModel(chapters = fakeChapters)

        composeTestRule.setContent {
            TableOfContentsScreen(
                bookId = 1,
                viewModel = fakeViewModel,
                onBack = {}
            )
        }

        repeat(3) { i ->
            composeTestRule.onNodeWithTag("toc_box").performClick()
            composeTestRule.waitForIdle()
            if (i % 2 == 0)
                composeTestRule.onNodeWithTag("fullscreen_text", useUnmergedTree = true).assertIsDisplayed()
            else
                composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()
        }
    }
}
