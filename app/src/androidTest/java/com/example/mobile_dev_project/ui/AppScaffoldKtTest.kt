package com.example.mobile_dev_project.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.ui.screens.HomeScreen
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AppScaffoldKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNavigationBar() {
        composeTestRule.setContent {
            MobileDevProjectTheme {
                BottomNavigationBar(navController = rememberNavController())
            }
        }

        composeTestRule.onNodeWithText("H").assertExists().assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("T").assertExists().assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("S").assertExists().assertIsDisplayed().performClick()
    }

}