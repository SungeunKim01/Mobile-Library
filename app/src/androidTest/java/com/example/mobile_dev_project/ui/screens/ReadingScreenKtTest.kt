package com.example.mobile_dev_project.ui.screens


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import com.example.mobile_dev_project.data.mockChapters
import com.example.mobile_dev_project.data.mockContents
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ReadingScreenKtTest {
    @get: Rule
    val composeTestRule = createComposeRule()
    private lateinit var fakeTTsVM: FakeTTsViewModel

    /**
     * Set up the compose rule with the ReadingScreen composable
     */
    @Before
    fun setUp() {
        val fakePosVM = FakePositionViewModel()
        val fakeRetrieveVM = FakeRetrieveDataViewModel(mockChapters, mockContents)
        fakeTTsVM = FakeTTsViewModel()

        composeTestRule.setContent {
            MobileDevProjectTheme {
                ReadingScreenForTest(
                    chapters = mockChapters,
                    contents = mockContents,
                    chapterIndexSelected = 0,
                    ttsVM = fakeTTsVM,
                    posVM = fakePosVM
                )
            }
        }
    }

    /**
     * Test that the search button is displayed and that it is clickable
     */
    @Test
    fun searchButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("search_btn").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("search_btn").performClick()

    }

    /**
     * Test that the back button is displayed and that it is clickable
     */
    @Test
    fun backButtonIsDisplayed(){
        composeTestRule.onNodeWithTag("back_btn").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("back_btn").performClick()
    }

    /**
     * Test that the chapter title is displayed
     */
    @Test
    fun chapterTitleIsDisplayed(){
        composeTestRule.onNodeWithTag("title", useUnmergedTree = true).assertExists().assertIsDisplayed()

    }

    /**
     * Test that the chapter content is displayed
     */
    @Test
    fun chapterContentIsDisplayed(){
        composeTestRule.onNodeWithTag("content", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }
    /**
     * Test that the chapter control bar is displayed
     */
    @Test
    fun ttsBarIsDisplayed(){
        composeTestRule.onNodeWithTag("tts_bar", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }


}
