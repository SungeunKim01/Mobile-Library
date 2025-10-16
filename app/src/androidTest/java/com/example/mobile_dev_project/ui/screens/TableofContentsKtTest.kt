package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TableOfContentsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // BASIC UI TESTS

    // Test 1: Verify that the title "Table of Contents" is visible on the screen
    @Test
    fun title_isDisplayed() {
        // Set up the composable under test
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // Locate the title Text by its test tag and verify it exists and is visible
        composeTestRule.onNodeWithTag("toc_title", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Table of Contents")
    }

    // Test 2: Ensure that all chapter titles appear on screen
    @Test
    fun chapters_areDisplayed() {
        // Create a list of test chapters
        val chapters = listOf(
            "Chapter 1: The Beginning",
            "Chapter 2: The Journey",
            "Chapter 3: The End"
        )

        // Display the composable with the provided chapter list
        composeTestRule.setContent {
            TableOfContentsScreen(chapters = chapters, onBack = {})
        }

        // For each chapter, assert that its text exists on screen
        chapters.forEach { chapter ->
            composeTestRule.onNodeWithText(chapter, useUnmergedTree = true)
                .assertIsDisplayed()
        }
    }

    // Test 3: Confirm that clicking a chapter calls the callback function
    @Test
    fun clickingChapter_callsCallback() {
        var selectedChapter: String? = null
        val chapters = listOf("Chapter 1: The Beginning")

        // Render the composable with a callback that records the selected chapter
        composeTestRule.setContent {
            TableOfContentsScreen(
                chapters = chapters,
                onChapterSelected = { selectedChapter = it },
                onBack = {}
            )
        }

        // Perform a click on the first chapter button
        composeTestRule.onNodeWithText("Chapter 1: The Beginning", useUnmergedTree = true)
            .performClick()

        // Verify that the callback was triggered with the correct value
        assert(selectedChapter == "Chapter 1: The Beginning")
    }

    // Test 4: Check that clicking the background toggles fullscreen text
    @Test
    fun clickingBackground_togglesFullscreenText() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // Initially, fullscreen text should not be visible
        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()

        // Click anywhere on the Box to enter immersive (fullscreen) mode
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle() // Wait for recomposition to finish

        // The fullscreen message should now be visible
        composeTestRule.onNodeWithTag("fullscreen_text", useUnmergedTree = true).assertIsDisplayed()

        // Click again to exit fullscreen mode
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle()

        // Verify that the fullscreen message disappears
        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()
    }

    // STRUCTURE AND HIERARCHY TESTS

    // Test 5: Check that the title is not inside the LazyColumn list
    @Test
    fun title_isInsideTopColumn() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // The title should exist and not be a child of the LazyColumn
        composeTestRule.onNodeWithTag("toc_title", useUnmergedTree = true)
            .assertExists()
            .assert(hasParent(hasTestTag("toc_list").not()))
    }

    // Test 6: Verify that each chapter button is a child of the LazyColumn
    @Test
    fun chapterButtons_areInsideLazyColumn() {
        val chapters = listOf("Chapter 1: The Beginning", "Chapter 2: The Journey")

        composeTestRule.setContent {
            TableOfContentsScreen(chapters = chapters, onBack = {})
        }

        // For each chapter, ensure the button is within the LazyColumn
        chapters.forEach { chapter ->
            composeTestRule.onNodeWithTag("chapter_button_$chapter", useUnmergedTree = true)
                .assertExists()
                .assert(hasParent(hasTestTag("toc_list")))
        }
    }

    // Test 7: Check that the floating back button exists and is inside the main Box
    @Test
    fun backButton_isInsideBoxAndVisible() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        composeTestRule.onNodeWithTag("back_button", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assert(hasParent(hasTestTag("toc_box")))
    }


    // Test 8: Confirm that the fullscreen text is inside the Box when active
    @Test
    fun fullscreenText_isInsideBox_whenVisible() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // Activate immersive mode
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.waitForIdle()

        // Verify that the fullscreen text appears as a child of the Box
        composeTestRule.onNodeWithTag("fullscreen_text", useUnmergedTree = true)
            .assertExists()
            .assert(hasParent(hasTestTag("toc_box")))
    }

    // EDGE CASE TESTS

    // Test 9: If no chapters are passed, the LazyColumn should exist but be empty
    @Test
    fun noChapters_showsEmptyList() {
        composeTestRule.setContent {
            TableOfContentsScreen(chapters = emptyList(), onBack = {})
        }

        // LazyColumn container should still exist
        composeTestRule.onNodeWithTag("toc_list", useUnmergedTree = true)
            .assertExists()

        // There should be zero visible chapter nodes
        composeTestRule.onAllNodesWithText("Chapter", useUnmergedTree = true)
            .assertCountEquals(0)
    }

    // Test 10: Verify that clicking multiple times toggles fullscreen correctly
    @Test
    fun multipleClicks_toggleFullscreenRepeatedly() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // Perform multiple clicks and verify alternating fullscreen visibility
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
