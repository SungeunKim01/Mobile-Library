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

    @Before
    fun setUp() {
        composeTestRule.setContent{
            MobileDevProjectTheme {
                ReadingScreen( chapters = mockChapters, chapterIndexSelected = 0, onSearch = {}, onBack = {})

            }
        }
    }
    @Test
    fun searchButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("search_btn").assertExists().assertIsDisplayed()

    }

    @Test
    fun backButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("back_btn").assertExists().assertIsDisplayed()
    }

    @Test
    fun chapterTitleIsDisplayed(){
        composeTestRule.onNodeWithTag("title").assertExists().assertIsDisplayed()

    }

    @Test
    fun chapterContentIsDisplayed(){
        composeTestRule.onNodeWithTag("content").assertExists().assertIsDisplayed()
    }

}