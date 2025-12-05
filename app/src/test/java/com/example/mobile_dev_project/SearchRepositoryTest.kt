package com.example.mobile_dev_project

import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.repository.SearchRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * this is unit tests for SearchRepository
 * mock the ContentDao and ChapterDao with mockk()
 * call suspend fun search(query: String) inside runTest
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryTest {

    // creating mock dao obj once and reuse in tests
    private val contentDao: ContentDao = mockk()
    private val chapterDao: ChapterDao = mockk()

    private val repository = SearchRepository(contentDao = contentDao, chapterDao = chapterDao)

    @Test
    fun blank_query_return_emptylist_and_no_call_dao() = runTest {
        // query str - spaces
        val query = "   "

        val results = repository.search(query)

        //should get empty list
        assertTrue(results.isEmpty())

        //SearchRepository should return early & never hit db so verify getAllContents & getAllChapters are not called
        verify(exactly = 0) { contentDao.getAllContents() }
        verify(exactly = 0) { chapterDao.getAllChapters() }
    }

    @Test
    fun query_with_no_match_return_emptylist() = runTest {
        // one chapter and one content that dont contain the word "boy"

        val chapter = Chapter(bookId = 1, chapterName = "Chapter 1", chapterOrder = 1, contentId = null
        ).apply { chapterId = 1 }

        val content = Content(chapterId = chapter.chapterId, content = "This text has no keyword."
        ).apply { contentId = 1 }

        every { chapterDao.getAllChapters() } returns flowOf(listOf(chapter))
        every { contentDao.getAllContents() } returns flowOf(listOf(content))

        val results = repository.search("boy")

        //should get empty list
        assertTrue(results.isEmpty())

        // dao should each be called once during this search
        verify(exactly = 1) { chapterDao.getAllChapters() }
        verify(exactly = 1) { contentDao.getAllContents() }
    }

    @Test
    fun single_match_build_correct_result_including_scroll_ratio_and_snippet() = runTest {
        // text w exactly one boy
        val text = "The boy was delighted with the prospect of seeing the great scientist Tesla, about whom he had read so much, and began to ask his older friend a thousand questions about the man, his work and life."
        val chapter = Chapter(bookId = 2, chapterName = "ARTIFICIAL LIGHTNING MADE AND HARNESSED TO MAN'S USE", chapterOrder = 1, contentId = null
        ).apply { chapterId = 10 }

        val content = Content(chapterId = chapter.chapterId, content = text
        ).apply { contentId = 20 }

        every { chapterDao.getAllChapters() } returns flowOf(listOf(chapter))
        every { contentDao.getAllContents() } returns flowOf(listOf(content))

        // query w spaces to test trim()
        val queryWithSpaces = "  boy  "

        val results = repository.search(queryWithSpaces)
        // should get exactly one SearchResult
        assertEquals(1, results.size)

        val result = results[0]
        assertEquals(2, result.bookId)
        assertEquals(10, result.chapterId)
        assertEquals(20, result.contentId)

        assertEquals("ARTIFICIAL LIGHTNING MADE AND HARNESSED TO MAN'S USE", result.chapterTitle)

        //repo trims the query before saving it in SearchResult
        assertEquals("boy", result.query)
        assertTrue(result.snippet.isNotBlank())
        assertTrue(result.snippet.contains("boy", ignoreCase = true))
        assertTrue(result.snippet.length <= text.length)
        //text is long so SearchRepository cuts it and add ... at the end
        assertTrue(result.snippet.endsWith("..."))

        // scrollRatio is computed
        val lowerText = text.lowercase()
        val matchIndex = lowerText.indexOf("boy")
        val expectedScrollRatio = matchIndex.toFloat() / text.length.toFloat()

        // check stored scrollRatio is close to expected
        assertEquals(expectedScrollRatio, result.scrollRatio, 0.0001f)
    }

    @Test
    fun null_chapterName_use_fallback_title_w_chapterOrder() = runTest {
        // chapter.chapterName is null, so SearchRepository use Chapter ${chapter.chapterOrder} as the title
        val chapter = Chapter(bookId = 1, chapterName = null, chapterOrder = 3, contentId = null
        ).apply { chapterId = 3 }

        val text = "The boy was delighted with the prospect of seeing the great scientist Tesla, about whom he had read so much, and began to ask his older friend a thousand questions about the man, his work and life."
        val content = Content(chapterId = chapter.chapterId, content = text
        ).apply {
            contentId = 30
        }

        every { chapterDao.getAllChapters() } returns flowOf(listOf(chapter))
        every { contentDao.getAllContents() } returns flowOf(listOf(content))

        val results = repository.search("boy")
        // should get one result
        assertEquals(1, results.size)

        val result = results[0]
        // repo should have used"Chapter 3 as title bc chapter title is null
        assertEquals("Chapter 3", result.chapterTitle)

        assertEquals(1, result.bookId)
        assertEquals(3, result.chapterId)
        assertEquals(30, result.contentId)
    }

    @Test
    fun multiple_matches_contents_and_caseInsensitive() = runTest {
        // 2 chapters & 2 contents so contain 4occurrences of boy in diff cases
        val chapter1 = Chapter(bookId = 1, chapterName = "Chapter One", chapterOrder = 1, contentId = null
        ).apply { chapterId = 1 }

        val chapter2 = Chapter(bookId = 1, chapterName = "Chapter Two", chapterOrder = 2, contentId = null
        ).apply { chapterId = 2 }

        // 3matches for boy in case insensitive search
        val content1 = Content(chapterId = chapter1.chapterId, content = "Boy boy BOY."
        ).apply { contentId = 1 }

        // 1match for boy
        val content2 = Content(
            chapterId = chapter2.chapterId,
            content = "The boy was delighted with the prospect of seeing the great scientist Tesla, about whom he had read so much, and began to ask his older friend a thousand questions about the man, his work and life."
            ).apply { contentId = 2 }

        every { chapterDao.getAllChapters() } returns flowOf(listOf(chapter1, chapter2))
        every { contentDao.getAllContents() } returns flowOf(listOf(content1, content2))

        val lowerResults = repository.search("boy")
        val upperResults = repository.search("BOY")

        // should give 4 totla matches
        assertEquals(4, lowerResults.size)
        assertEquals(lowerResults.size, upperResults.size)
    }
}
