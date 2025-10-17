package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.MainActivity
import com.example.mobile_dev_project.nav.Route
import com.example.mobile_dev_project.ui.AppNavHost
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * hilt nav test for Download Book screen
 * I set content to AppNavHost(start=download) then press back and expect Home
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DownloadNavTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @Before fun setup() { hiltRule.inject() }

    @Test
    fun addNewBook_navigates_to_download_and_cancel_returns_home() {
        // start at Home because AppScaffold uses startDestination = Route.Home.route
        compose.onNodeWithTag("home_screen").assertIsDisplayed()

        // go to Download Book
        compose.onNodeWithTag("AddNewBookButton").assertIsDisplayed().performClick()

        //on Download Book (Url field present)
        compose.onNodeWithTag("UrlField").assertIsDisplayed()

        //hit Cancel and verify we return Home
        compose.onNodeWithTag("CancelButton").assertIsDisplayed().performClick()
        compose.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun addNewBook_navigates_to_download_and_back_returns_home() {
        //home visible
        compose.onNodeWithTag("home_screen").assertIsDisplayed()

        // go to Download
        compose.onNodeWithTag("download_button").assertIsDisplayed().performClick()
        compose.onNodeWithTag("UrlField").assertIsDisplayed()

        // tap TopAppBar Back (AutoMirrored arrow)
        compose.onNodeWithTag("BackButton").assertIsDisplayed().performClick()

        //back at Home
        compose.onNodeWithTag("home_screen").assertIsDisplayed()
    }
}