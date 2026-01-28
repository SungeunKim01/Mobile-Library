package com.example.mobile_dev_project.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// tests for AppScaffold and bottom navigation
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppScaffoldKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun bottomNavigationBar_items_areVisible_and_TOC_doesNothing_whenNoBookSelected() {
        composeRule.onNodeWithTag("home_screen").assertIsDisplayed()

        composeRule.onNodeWithText("Home").assertIsDisplayed()
        composeRule.onNodeWithText("Search").assertIsDisplayed()
        composeRule.onNodeWithText("Content").assertIsDisplayed()
        composeRule.onNodeWithText("Download").assertIsDisplayed()

        composeRule.onNodeWithText("Content").performClick()

        composeRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }
}
