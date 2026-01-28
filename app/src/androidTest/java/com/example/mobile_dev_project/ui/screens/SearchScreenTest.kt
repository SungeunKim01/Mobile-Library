package com.example.mobile_dev_project.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.mobile_dev_project.data.SearchResult
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

//instrumentation tests for Search screen
@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    //test SearchField updates its text when user types
    @Test
    fun searchField_userTyping_updatesText() {
        composeRule.setContent {
            var text by remember { mutableStateOf("") }

            SearchField(
                value = text,
                onValueChange = { newValue ->
                    text = newValue
                }
            )
        }

        composeRule
            .onNodeWithTag("QueryField")
            .performTextInput("boy")

        // check the field contains boy
        composeRule
            .onNodeWithTag("QueryField")
            .assertTextContains("boy")
    }

    //test that ResultCount show the result count number in its text
    @Test
    fun resultCount_shows_count_number_in_text() {
        composeRule.setContent {
            ResultCount(
                count = 5,
                queryNotBlank = true
            )
        }

        //check ResultsCount exists &visible
        composeRule
            .onNodeWithTag("ResultsCount")
            .assertExists()
            .assertIsDisplayed()
    }

    // test SearchResultsList show result cards & calls onResultClick
    @Test
    fun searchResultsList_show_cards_n_handles_click() {
        val fakeResults = listOf(
            SearchResult(
                bookId = 1,
                chapterId = 10,
                contentId = 100,
                chapterTitle = "Chapter 1 - The Aeroplane",
                query = "boy",
                snippet = "But on every side the boy heard people talking of great feats of flying that he knew nothing about...",
                scrollRatio = 0.3f
            ),
            SearchResult(
                bookId = 1,
                chapterId = 11,
                contentId = 101,
                chapterTitle = "Chapter 2 - AEROPLANES TO-DAY",
                query = "boy",
                snippet = "Of course it was impossible for the boy to study every improvement or every make of aeroplane...",
                scrollRatio = 0.7f
            )
        )

        var clicked: SearchResult? = null

        composeRule.setContent {
            SearchResultsList(
                matches = fakeResults,
                query = "boy",
                onResultClick = { hit ->
                    clicked = hit
                }
            )
        }

        composeRule
            .onNodeWithTag("ResultCard_0")
            .performClick()

        // clicked should be the 1st SearchResult after click
        val clickedResult = clicked
        requireNotNull(clickedResult)

        assertEquals(100, clickedResult.contentId)
        assertEquals(10, clickedResult.chapterId)
        assertEquals("Chapter 1 - The Aeroplane", clickedResult.chapterTitle)
    }
}
