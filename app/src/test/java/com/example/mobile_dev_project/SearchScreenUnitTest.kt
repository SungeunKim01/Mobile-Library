package com.example.mobile_dev_project

import com.example.mobile_dev_project.ui.screens.SearchViewModel
import com.example.mobile_dev_project.ui.screens.findMatches
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * unit tests for SearchViewModel
 * this verifie vm correctly computes the list of matches based on the user's query using the sample paragraphs
 */
class SearchScreenUnitTest {
    @Test
    fun blank_query_returns_empty_list() {
        val vm = SearchViewModel()

        // test empty string
        vm.onQueryChanged("")
        assertEquals(emptyList<Pair<Int, String>>(), vm.matches)

        // test whitespace
        vm.onQueryChanged("   ")
        assertEquals(emptyList<Pair<Int, String>>(), vm.matches)
    }

    @Test
    fun finds_single_match_case_insensitive() {
        val vm = SearchViewModel()

        // "vanishing" should match the 2nd item (index = 1)
        vm.onQueryChanged("vanishing")

        val expected = listOf(1 to "Chapter 2 : THE VANISHING GLASS")
        assertEquals(expected, vm.matches)
    }

    @Test
    fun finds_multiple_matches_when_query_is_common_word() {
        val vm = SearchViewModel()

        // "chapter" appears in all four items
        vm.onQueryChanged("Chapter")

        val expected = listOf(
            0 to "Chapter 1 : THE BOY WHO LIVED",
            1 to "Chapter 2 : THE VANISHING GLASS",
            2 to "Chapter 3 : THE LETTERS FROM NO ONE",
            3 to "Chapter 4 : THE KEEPER OF THE KEYS"
        )

        assertEquals(expected, vm.matches)
    }

    @Test
    fun no_match_returns_empty_list() {
        val vm = SearchViewModel()

        // test string not present
        vm.onQueryChanged("nomatch")
        assertEquals(emptyList<Pair<Int, String>>(), vm.matches)
    }

}