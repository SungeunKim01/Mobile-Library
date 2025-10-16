package com.example.mobile_dev_project

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import com.example.mobile_dev_project.ui.screens.DownloadBookScreen

// instrumentation ui tests (Compose) - this verify actual screen behavior liike:
// - Add button disabled on start
// - Typing Ul enables Add
// - Cancel/Back are clickable
// This confirms the Ui wiring w/o involving navigation and this is fast and focused
// Refer "compose testing cheatsheet - setContent, finders, assertions"
class DownloadBookScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun add_disabled_initially_then_enabled_after_typing_url() {

        // render the screen in isolation - here, no NavHost needed for this test
        rule.setContent { DownloadBookScreen(onBack = {}) }

        //Initially disabled
        rule.onNodeWithTag("AddButton").assertIsDisplayed().assertIsNotEnabled()

        // Type a ural into the field
        rule.onNodeWithTag("UrlField").performTextInput("https://ex.com/book.html")

        // now enabled, so canEnableAdd(url) == true
        rule.onNodeWithTag("AddButton").assertIsEnabled()
    }

    @Test
    fun cancel_and_back_buttons_are_clickable() {
        var backCalls = 0
        rule.setContent { DownloadBookScreen(onBack = { backCalls++ }) }

        // Clicking both should not crash, just increments backCalls
        rule.onNodeWithTag("BackButton").performClick()
        rule.onNodeWithTag("CancelButton").performClick()
    }
}