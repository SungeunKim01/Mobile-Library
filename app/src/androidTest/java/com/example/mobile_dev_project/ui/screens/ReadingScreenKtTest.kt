package com.example.mobile_dev_project.ui.screens


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import com.example.mobile_dev_project.data.mockChapters
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReadingScreenKtTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    /**
     * Set up the compose rule with the ReadingScreen composable
     */
    @Before
    fun setUp() {
        composeTestRule.setContent{
            MobileDevProjectTheme {
                ReadingScreen( chapters = mockChapters, chapterIndexSelected = 0, onSearch = {}, onBack = {})

            }
        }
    }

    /**
     * Test that the search button is displayed and that it is clickable
     */
    @Test
    fun searchButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("search_btn").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("search_btn").performClick()

    }

    /**
     * Test that the back button is displayed and that it is clickable
     */
    @Test
    fun backButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("back_btn").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("back_btn").performClick()
    }

    /**
     * Test that the chapter title is displayed
     */
    @Test
    fun chapterTitleIsDisplayed(){
        composeTestRule.onNodeWithTag("title").assertExists().assertIsDisplayed()

    }

    /**
     * Test that the chapter content is displayed
     */
    @Test
    fun chapterContentIsDisplayed(){
        composeTestRule.onNodeWithTag("content").assertExists().assertIsDisplayed()
    }

}