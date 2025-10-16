package com.example.mobile_dev_project

import com.example.mobile_dev_project.ui.screens.findMatches
import org.junit.Assert.assertEquals
import org.junit.Test


//test findMatches(paragraphs, query) unction
class SearchScreenUnitTest {
    private val sampleContent = listOf(
        "Chapter 1 : THE BOY WHO LIVED",
        "Chapter 2 : THE VANISHING GLASS",
        "Chapter 3 : THE LETTERS FROM NO ONE",
        "Chapter 4 : THE KEEPER OF THE KEYS"
    )

    @Test
    fun blank_query_returns_empty_list() {
        assertEquals(emptyList<Pair<Int, String>>(), findMatches(sampleContent, ""))
        assertEquals(emptyList<Pair<Int, String>>(), findMatches(sampleContent, "   "))
    }

    @Test
    fun finds_single_match_case_insensitive() {
        val result = findMatches(sampleContent, "vanishing")
        //expect one match: index 1
        assertEquals(listOf(1 to sampleContent[1]), result)
    }

    @Test
    fun finds_multiple_matches() {
        val result = findMatches(sampleContent, "Chapter")
        //all lines contain the word, "Chapter"
        val expected = sampleContent.indices.map { it to sampleContent[it] }
        assertEquals(expected, result)
    }

    @Test
    fun no_match_returns_empty_list() {
        assertEquals(emptyList<Pair<Int, String>>(), findMatches(sampleContent, "nomatch"))
    }

}