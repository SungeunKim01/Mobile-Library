package com.example.mobile_dev_project.data

/**
 * ui model for one search hit and keeps
 * -which book & chapter the match is in
 * - which content row it matched so can reuse scroll logic
 * -ch title to show in the results list
 * - snippet - short piece of text around the match
 */
data class SearchResult(
    val bookId: Int,
    val chapterId: Int,
    val contentId: Int,
    val chapterTitle: String,
    val snippet: String,
    val scrollRatio: Float,
    val query: String
)


