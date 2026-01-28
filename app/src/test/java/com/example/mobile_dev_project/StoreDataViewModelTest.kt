package com.example.mobile_dev_project.ui.screens

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import com.example.mobile_dev_project.data.repository.ParsingRepository

@OptIn(ExperimentalCoroutinesApi::class)
class StoreDataViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var book_repo: BookRepository
    private lateinit var chapter_repo: ChapterRepository
    private lateinit var content_repo: ContentRepository
    private lateinit var parsing_repo: ParsingRepository

    private lateinit var view_model: StoreDataViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        book_repo = mockk()
        chapter_repo = mockk()
        content_repo = mockk()
        parsing_repo = mockk()

        view_model = StoreDataViewModel(
            bookRepository = book_repo,
            chapterRepository = chapter_repo,
            contentRepository = content_repo,
            parsingRepository = parsing_repo
        )
    }

    @After
    fun tear_down() {
        Dispatchers.resetMain()
    }

    @Test
    fun store_book_data_inserts_book_chapters_and_content() = runTest {
        val chapter = UiChapter(chapterId = 1, chapterTitle = "Chapter 1", chapterOrder = 1, bookId = 0)
        val content = UiContent(contentId = 1, chapterId = 1, content = "Content 1")
        val test_book = UiBook(title = "Test Book", chapters = listOf(chapter))
        val source_url = "http://example.com/book.html"

        coEvery { book_repo.urlExists(source_url) } returns false
        coEvery { book_repo.bookExists(test_book.title) } returns false
        coEvery { book_repo.insertBook(any()) } returns 123L
        coEvery { parsing_repo.parseHtml("123") } returns Pair(
            test_book.copy(chapters = listOf(chapter)),
            listOf(content)
        )
        coEvery { chapter_repo.insertChapter(any()) } returns 456L
        coEvery { content_repo.insertContent(any()) } just Runs

        view_model.storeBookData(test_book, source_url)
        testDispatcher.scheduler.advanceUntilIdle() // wait for coroutine

        coVerify { book_repo.insertBook(any()) }
        coVerify { parsing_repo.parseHtml("123") }
        coVerify { chapter_repo.insertChapter(any()) }
        coVerify { content_repo.insertContent(any()) }
    }
}
