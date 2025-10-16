package com.example.mobile_dev_project.ui.screens

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * hvm unit tests for DownloadBookViewModel
 * this verifies the "Add to Library" enable rule (canAdd) reacts to url changes
 */

class DownloadBookScreenUnitTest {

    @Test
    fun canAdd_is_false_initially_and_for_blank_or_whitespace() {
        val vm = DownloadBookViewModel()

        // initial state
        assertFalse(vm.canAdd)

        // tests blank
        vm.onUrlChanged("")
        assertFalse(vm.canAdd)

        //tes whitespace
        vm.onUrlChanged("   \t\n")
        assertFalse(vm.canAdd)
    }

    @Test
    fun canAdd_becomes_true_when_url_is_non_blank() {
        val vm = DownloadBookViewModel()

        vm.onUrlChanged("https://ex.com/book.html")
        assertTrue(vm.canAdd)

        // still true with leading or trailing spaces, current M1 rule is "non blank"
        vm.onUrlChanged("   http://exam.com/ebooks/ch1.html   ")
        assertTrue(vm.canAdd)
    }
}