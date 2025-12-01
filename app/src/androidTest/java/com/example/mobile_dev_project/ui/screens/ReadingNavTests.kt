package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
import com.example.mobile_dev_project.data.mockChapters
import com.example.mobile_dev_project.data.mockContents
import com.example.mobile_dev_project.data.mockBook
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ReadingNavigationTests {
    //source: https://gitlab.com/crdavis/navigationtestingexamplecode/-/blob/master/app/src/androidTest/java/com/example/navigationtestingexamplecode/MainNavigationTest.kt?ref_type=heads
    // di
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    //ui test
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Inject Hilt dependencies
        hiltRule.inject()
    }j

    @Test
    fun searchBtn_NavigatesToSearch() {
        val book = mockBook
        val chapters = mockChapters
        val contents = mockContents
        composeTestRule.setContent {
            ReadingScreenForTest(
                mockChapters, mockContents,1,{},{}
            )
        }
        // the title of the reading screen is displayed
        composeTestRule.onNodeWithTag("title").assertIsDisplayed()


        //going to search
        composeTestRule.onNodeWithText("search").assertIsDisplayed().performClick()
        composeTestRule.onAllNodesWithText("Search").filterToOne(hasTestTag("QueryField")).assertIsDisplayed()

    }
    @Test
    fun backBtn_NavigatesToToc() {
        val book = mockBook
        val chapters = mockChapters
        val contents = mockContents
        composeTestRule.setContent {
            ReadingScreenForTest(
                mockChapters, mockContents,1,{},{}
            )
        }
        // the title of the reading screen is displayed
        composeTestRule.onNodeWithTag("title").assertIsDisplayed()

        //going BACK to table of contents
        composeTestRule.onNodeWithText("←").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

    }

}