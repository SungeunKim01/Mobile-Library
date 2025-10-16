package com.example.mobile_dev_project.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import com.example.mobile_dev_project.data.mockChapters
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTests {
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
    }

    @Test
    fun bottomBar_TNavigatesToTableOfContents() {
        // in bottom bar "T" represents the Table of Contents screen
        // click on T => table of contents screen
        composeTestRule.onNodeWithText("T").assertIsDisplayed()
        composeTestRule.onNodeWithText("T").performClick()

        // in table of contents screen, the following text is displayed:
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

    }
    @Test
    fun bottomBar_HNavigatesToHome(){
        // in bottom bar, "H" represents the Home screen.
        // clicking on H => home screen
        composeTestRule.onNodeWithText("H").assertIsDisplayed()
        composeTestRule.onNodeWithText("H").performClick()

        // in home screen, theres a welcome message
        composeTestRule.onNodeWithText("Welcome to Mobile Library").assertIsDisplayed()
    }
    @Test
    fun bottomBar_SNavigatesToSearch(){
        // in bottom bar, "S" represents search screen.
        // clicking on S => search screen
        composeTestRule.onNodeWithText("S").performClick()
        composeTestRule.onNodeWithText("S").assertIsDisplayed()

        // in search screen, theres a text "Search"
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }
    @Test
    fun addBookBtn_NavigatesToDownload(){
        //clicking on add new book, in home page, should navigate to download screen
        composeTestRule.onNodeWithText("Add New Book").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add New Book").performClick()

        composeTestRule.onAllNodesWithText("Download Book").filterToOne().assertIsDisplayed()

        // cancel button in download screen takes u back to home
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Welcome to Mobile Library").assertIsDisplayed()

    }





}