package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
import com.example.mobile_dev_project.ui.AppScaffold
import com.example.mobile_dev_project.ui.BottomNavigationBar
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

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
                AppScaffold(nav = rememberNavController())
            }
        }

        composeTestRule.onNodeWithTag("download_button")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        //This is testing if the download page is shown after the click is pressed, and I dont want to change another persons code this will do
        composeTestRule.onNodeWithTag("UrlField")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("AddButton")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("CancelButton")
            .assertExists()
            .assertIsDisplayed()
    }
}