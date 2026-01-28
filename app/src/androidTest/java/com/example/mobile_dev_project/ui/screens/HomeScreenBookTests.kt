package com.example.mobile_dev_project.ui.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeScreenBookTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    /**
     * Set up the compose rule with the HomeScreen composable
     */
    @Before
    fun setUp() {
        val fakeVm = FakeHomeScreenViewModel()

        composeTestRule.setContent {
            MobileDevProjectTheme {
                HomeScreen(
                    viewModel = fakeVm,
                    onNavigateToDownload = {},
                    onNavigateToContents = {}
                )
            }
        }
    }
    @Test
    fun homeScreenWithFakeBooks_displaysBooks() {


        composeTestRule.onNodeWithTag("home_screen").assertExists().assertIsDisplayed()
        val books =  composeTestRule.onAllNodesWithTag("book", useUnmergedTree = true).assertCountEquals(4)
        books[0].assertExists().assertIsDisplayed()
        books[1].assertExists().assertIsDisplayed()
        books[2].assertExists().assertIsDisplayed()
        books[3].assertExists().assertIsDisplayed()

    }

    //Tests if bookserlf exists and is displayed
    @Test
    fun bookself() {


        composeTestRule.onNodeWithTag("bookshelf", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }
}