package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
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
    //Tests if homescreen exists and is displayed
    @Test
    fun homeScreen() {

        composeTestRule.onNodeWithTag("home_screen")
            .assertExists()
            .assertIsDisplayed()
    }
    //Tests if title exists and is displayed
    @Test
    fun restaurantTitle() {


        composeTestRule.onNodeWithTag("title")
            .assertExists()
            .assertIsDisplayed()
    }
    //Tests if logo exists and is displayed
    @Test
    fun restaurantLogo() {


        composeTestRule.onNodeWithTag("restaurant_logo")
            .assertExists()
            .assertIsDisplayed()
    }

//Tests if bookserlf exists and is displayed
    @Test
    fun bookself() {


        composeTestRule.onNodeWithTag("bookshelf")
            .assertExists()
            .assertIsDisplayed()
    }
//This will go each book, and
// then will test if each exist and if the count is 4 and is displayed, I know this is hardcoded, I will try to make it more flexible
    @Test
    fun book() {
       val books =  composeTestRule.onAllNodesWithTag("book").assertCountEquals(4)
            books[0].assertExists().assertIsDisplayed()
            books[1].assertExists().assertIsDisplayed()
            books[2].assertExists().assertIsDisplayed()
            books[3].assertExists().assertIsDisplayed()
        }



    //Tests if book button exists and is displayed and if the click works and if the other screen show shows up
    @Test
    fun downloadBookButton() {



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