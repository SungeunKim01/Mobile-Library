//package com.example.mobile_dev_project.ui.screens
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.performClick
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.mobile_dev_project.data.UiContent
//import com.example.mobile_dev_project.data.mockChapters
//import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
//import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class ReadingScreenKtTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private lateinit var mockContents: List<UiContent>
//
//    @Before
//    fun setUp() {
//        mockContents = mockChapters.map { chapter ->
//            UiContent(
//                contentId = chapter.contentId ?: 0,
//                chapterId = chapter.chapterId ?: 0,
//                content = "Content for ${chapter.chapterTitle}"
//            )
//        }
//
//        composeTestRule.setContent {
//            MobileDevProjectTheme {
//                ReadingScreenForTest(
//                    chapters = mockChapters,
//                    contents = mockContents,
//                    chapterIndexSelected = 0,
//                    onSearch = {},
//                    onBack = {}
//                )
//            }
//        }
//
//    }
//
//    @Test
//    fun searchButtonIsDisplayedAndClickable() {
//        composeTestRule.onNodeWithTag("search_btn").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("search_btn").performClick()
//    }
//
//    @Test
//    fun backButtonIsDisplayedAndClickable() {
//        composeTestRule.onNodeWithTag("back_btn").assertIsDisplayed()
//        composeTestRule.onNodeWithTag("back_btn").performClick()
//    }
//
//    @Test
//    fun chapterTitleIsDisplayed() {
//        composeTestRule.onNodeWithTag("title").assertIsDisplayed()
//    }
//
//    @Test
//    fun chapterContentIsDisplayed() {
//        composeTestRule.onNodeWithTag("content").assertIsDisplayed()
//    }
//}
//
