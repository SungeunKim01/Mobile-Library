package com.example.mobile_dev_project

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import com.example.mobile_dev_project.ui.screens.SearchScreen


//Instrumentation ui testsfor SearchScreen
//typing into QueryField shows result cards and updates count text
///I refer compose testing cheatsheet (setContent, finders, assertions)

class SearchScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun typing_known_text_shows_result_card() {
        rule.setContent { SearchScreen(onBack = {}) }
        //yype smt present in sample paragraphs
        rule.onNodeWithTag("QueryField").performTextInput("Chapter 2")
        // 1st result card should appear
        rule.onNodeWithTag("ResultCard_0").assertIsDisplayed()
        //Count label visible
        rule.onNodeWithTag("ResultsCount").assertIsDisplayed()
    }

    @Test
    fun typing_query_shows_multiple_cards() {
        rule.setContent { SearchScreen(onBack = {}) }
        rule.onNodeWithTag("QueryField").performTextInput("Chapter")
        // here, at least 2 result cards should exist for "Chapter" bc there's 4 paragraphs that match to query ("Chapter")
        rule.onNodeWithTag("ResultCard_0").assertIsDisplayed()
        rule.onNodeWithTag("ResultCard_1").assertIsDisplayed()
    }
}
