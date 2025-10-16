package com.example.mobile_dev_project.ui.screens

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test -JVM, this is fast and need no emulator
 * tests the pure logic used by DownloadBookScreen's Add button: canEnableAdd(url)
 * non blank => enabled, so matches my current implementation
 */

class DownloadBookScreenUnitTest {

    @Test
    fun blank_is_rejected() {
        assertFalse(canEnableAdd(""))
    }

    @Test
    fun whitespace_only_is_rejected() {
        assertFalse(canEnableAdd(" "))
        assertFalse(canEnableAdd("   \t\n"))
    }

    @Test
    fun any_non_blank_is_accepted() {
        assertTrue(canEnableAdd("x"))
        assertTrue(canEnableAdd("https://example.org/book.html"))
        assertTrue(canEnableAdd("  http://gutenberg.org/ebooks/1342.html  "))
    }
}