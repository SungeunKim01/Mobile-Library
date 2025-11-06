package com.example.mobile_dev_project.ui.screens

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository

class RetrieveDataViewModelUnitTest {

    private val fakeBookRepo = mockk<BookRepository>()
    private val fakeChapterRepo = mockk<ChapterRepository>()
    private val fakeContentRepo = mockk<ContentRepository>()

    private val viewModel = RetrieveDataViewModel(
        bookRepository = fakeBookRepo,
        chapterRepository = fakeChapterRepo,
        contentRepository = fakeContentRepo
    )

    @Test
    fun getBookshelf_returns_books() = runTest {
        val expectedEntities = listOf(
            Book("Test Book", "coverPath", "2025-01-01", "https://example.com")
        )

        every { fakeBookRepo.allBooks } returns flowOf(expectedEntities)

        val result = viewModel.getBookshelf().first()
        assertEquals(listOf("Test Book"), result.map { it.title })
    }

    @Test
    fun getChaptersForBook_returns_chapters() = runTest {
        val bookId = 1
        val expectedEntities = listOf(
            Chapter(bookId, "Chapter 1", 1, null)
        )

        every { fakeChapterRepo.getChaptersForBook(bookId) } returns flowOf(expectedEntities)

        val result = viewModel.getChaptersForBook(bookId).first()
        assertEquals(listOf("Chapter 1"), result.map { it.chapterTitle })
    }

    @Test
    fun getContentForChapter_returns_content() = runTest {
        val chapId = 1
        val expectedEntity = Content(chapId, "This is content")

        every { fakeContentRepo.getContentForChapter(chapId) } returns flowOf(expectedEntity)

        val result = viewModel.getContentForChapter(chapId).first()
        assertEquals("This is content", result?.content)
    }
}
