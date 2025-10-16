package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }
        composeTestRule.onNodeWithTag("home_screen")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun restaurantTitle() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }

        composeTestRule.onNodeWithTag("title")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun restaurantLogo() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }

        composeTestRule.onNodeWithTag("restaurant_logo")
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun bookself() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }

        composeTestRule.onNodeWithTag("bookshelf")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun book() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }

        composeTestRule.onNodeWithTag("book")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun downloadBookButton() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen()
            }
        }

        composeTestRule.onNodeWithTag("download_button")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
    }
}