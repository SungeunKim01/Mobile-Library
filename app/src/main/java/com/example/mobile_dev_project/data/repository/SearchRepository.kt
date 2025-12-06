package com.example.mobile_dev_project.data.repository

import com.example.mobile_dev_project.data.SearchResult
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import kotlinx.coroutines.flow.first
import javax.inject.Inject


//Repo for searching inside all saved book content
class SearchRepository @Inject constructor(
    private val contentDao: ContentDao,
    private val chapterDao: ChapterDao
) {

    /**
     * search inside all contents for the given query
     * every occurrence, build a SearchResult w bookId, chapterId, contentId, chapterTitle
     * , snippet - 60 chars before & full match & 140 chars after
     */
    suspend fun search(query: String): List<SearchResult> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return emptyList()
        }

        // Load all contents & chapters once, then work in memory
        val allContents: List<Content> = contentDao.getAllContents().first()
        val allChapters = chapterDao.getAllChapters().first()

        val chaptersById = mutableMapOf<Int, Chapter>()
        for (chapter in allChapters) {
            chaptersById[chapter.chapterId as Int] = chapter
        }

        val lowerQuery = trimmed.lowercase()
        val results = mutableListOf<SearchResult>()

        // through each content row and look for the query
        for (content in allContents) {
            val fullText = content.content ?: continue
            val lowerText = fullText.lowercase()

            var currentIndex = lowerText.indexOf(lowerQuery)
            while (currentIndex >= 0) {
                // build a snippet for this one occurrence
                val snippet = buildSnippet(
                    fullText = fullText,
                    matchIndex = currentIndex,
                    matchLength = trimmed.length
                )

                // how far down the content this match is
                val scrollRatio =
                    if (fullText.isNotEmpty()) {
                        currentIndex.toFloat() / fullText.length.toFloat()
                    } else {
                        0f
                    }

                //find chapter & book for this content
                val chapter = chaptersById[content.chapterId]
                if (chapter != null) {
                    val chapterTitle = chapter.chapterName ?: "Chapter ${chapter.chapterOrder}"

                    results += SearchResult(
                        bookId = chapter.bookId,
                        chapterId = chapter.chapterId,
                        contentId = content.contentId,
                        chapterTitle = chapterTitle,
                        snippet = snippet,
                        scrollRatio = scrollRatio,
                        query = trimmed
                    )
                }

                // look for nxt occurrence inside the same content
                val nextStart = currentIndex + lowerQuery.length
                currentIndex = lowerText.indexOf(lowerQuery, startIndex = nextStart)
            }
        }

        // keep the result that belong to the latest chapter for identical snippets in the same book/query
        val grouped: Map<Triple<Int, String, String>, List<SearchResult>> =
            results.groupBy { Triple(it.bookId, it.snippet, it.query) as Triple<Int, String, String> }

        //for each group, keep SearchResult whose chapterId is largest
        val unique: List<SearchResult> = grouped.values.map { group: List<SearchResult> ->
            group.maxByOrNull { it.chapterId ?: Int.MIN_VALUE }!!
        }

        return unique
    }

    /**
     * build a context snippet around one match
     * - 60 chars before the match
     * - full match
     * - 140 chas after the match
     * - prefix & suffix ... if cut off beginning & end
     */
    private fun buildSnippet(
        fullText: String,
        matchIndex: Int,
        matchLength: Int
    ): String {
        val start = maxOf(0, matchIndex - 60)
        val end = minOf(fullText.length, matchIndex + matchLength + 140)

        val prefix = if (start > 0) {
            "..."
        } else {
            ""
        }
        val suffix = if (end < fullText.length) {
            "..."
        } else {
            ""
        }

        val core = fullText.substring(start, end)

        return prefix + core + suffix
    }
}