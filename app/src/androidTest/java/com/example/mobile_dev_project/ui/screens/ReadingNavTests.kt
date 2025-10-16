package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
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
    }

    @Test
    fun bottomBar_TNavigatesToTableOfContentsToReadingScreen() {
        // in bottom bar "T" represents the Table of Contents screen
        // click on T => table of contents screen
        composeTestRule.onNodeWithText("T").assertIsDisplayed()
        composeTestRule.onNodeWithText("T").performClick()

        // in table of contents screen, the following text is displayed:
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

        // in table of contents, clicking on chapter leads to reading screen
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("progris riport 1").performClick()

        // the title of the reading screen is displayed
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()
    }

    @Test
    fun searchBtn_NavigatesToSearch() {

        //SAME THING AS LAST TEST. JUST ONE MORE STEP.
        // in bottom bar "T" represents the Table of Contents screen
        // click on T => table of contents screen
        composeTestRule.onNodeWithText("T").assertIsDisplayed()
        composeTestRule.onNodeWithText("T").performClick()

        // in table of contents screen, the following text is displayed:
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

        // in table of contents, clicking on chapter leads to reading screen
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("progris riport 1").performClick()

        // the title of the reading screen is displayed
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()


        //going to search
        composeTestRule.onNodeWithText("search").assertIsDisplayed().performClick()
        composeTestRule.onAllNodesWithText("Search").filterToOne(hasTestTag("QueryField")).assertIsDisplayed()

    }
    @Test
    fun backBtn_NavigatesToSearch() {

        //SAME THING AS LAST TEST. JUST ONE MORE STEP.
        // in bottom bar "T" represents the Table of Contents screen
        // click on T => table of contents screen
        composeTestRule.onNodeWithText("T").assertIsDisplayed()
        composeTestRule.onNodeWithText("T").performClick()

        // in table of contents screen, the following text is displayed:
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

        // in table of contents, clicking on chapter leads to reading screen
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("progris riport 1").performClick()

        // the title of the reading screen is displayed
        composeTestRule.onNodeWithText("progris riport 1").assertIsDisplayed()


        //going BACK to table of contents
        composeTestRule.onNodeWithText("←").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Table of Contents").assertIsDisplayed()

    }

}