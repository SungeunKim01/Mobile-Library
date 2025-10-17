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
 * hilt nav test for Search screen
 * start from MainActivity (AppScaffold and BottomBar), tap "S" to go to Search,
 * then press top bar back (the BackButton tag) to return to Home
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchNavTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // here, launch the real activity
    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @Before fun setup() { hiltRule.inject() }

    @Test
    fun bottomBar_S_navigatesToSearch_and_back_returnsHome() {
        //Bottom bar shows "S"
        compose.onNodeWithText("S").assertIsDisplayed().performClick()

        // onSearch screen, the Query TextField has this tag
        compose.onNodeWithTag("QueryField").assertIsDisplayed()

        //tap top app bar back button
        compose.onNodeWithTag("BackButton").assertIsDisplayed().performClick()

        //should be back on Home
        compose.onNodeWithTag("home_screen").assertIsDisplayed()
    }
}