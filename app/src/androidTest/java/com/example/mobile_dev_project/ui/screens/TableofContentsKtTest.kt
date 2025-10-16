package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TableOfContentsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun title_isDisplayed() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        composeTestRule.onNodeWithTag("toc_title")
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Table of Contents")
    }

    @Test
    fun chapters_areDisplayed() {
        val chapters = listOf("Chapter 1: The Beginning", "Chapter 2: The Journey", "Chapter 3: The End")

        composeTestRule.setContent {
            TableOfContentsScreen(chapters = chapters, onBack = {})
        }

        chapters.forEach { chapter ->
            composeTestRule.onNodeWithText(chapter).assertIsDisplayed()
        }
    }

    @Test
    fun clickingChapter_callsCallback() {
        var selectedChapter: String? = null
        val chapters = listOf("Chapter 1: The Beginning")

        composeTestRule.setContent {
            TableOfContentsScreen(
                chapters = chapters,
                onChapterSelected = { selectedChapter = it },
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Chapter 1: The Beginning").performClick()

        assert(selectedChapter == "Chapter 1: The Beginning")
    }

    @Test
    fun clickingBackground_togglesFullscreenText() {
        composeTestRule.setContent {
            TableOfContentsScreen(onBack = {})
        }

        // Fullscreen text should not be visible initially
        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()

        // Click anywhere on the box to enter fullscreen
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.onNodeWithTag("fullscreen_text").assertIsDisplayed()

        // Click again to exit fullscreen
        composeTestRule.onNodeWithTag("toc_box").performClick()
        composeTestRule.onNodeWithTag("fullscreen_text").assertDoesNotExist()
    }
}
