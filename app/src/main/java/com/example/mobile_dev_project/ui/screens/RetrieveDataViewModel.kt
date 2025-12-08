package com.example.mobile_dev_project.ui.screens


import androidx.lifecycle.ViewModel
import com.example.mobile_dev_project.data.repository.*
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import javax.inject.Inject
import com.example.mobile_dev_project.data.mappers.*
import com.example.mobile_dev_project.data.mockBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@HiltViewModel
open class RetrieveDataViewModel @Inject constructor(
    private val bookRepository: BookRepository?,
    private val chapterRepository: ChapterRepository?,
    private val contentRepository: ContentRepository?
) : ViewModel() {
    open fun getBookshelf(): Flow<List<UiBook>> {
        return bookRepository?.allBooks?.map { books ->
            books.map { it.toUi() }
        } ?: flowOf(emptyList())
    }
    open fun getChaptersForBook(bookId: Int): Flow<List<UiChapter>> {
        return chapterRepository?.getChaptersForBook(bookId)
            ?.map { chapters -> chapters.map { it.toUi() } } ?:flowOf(emptyList())
    }
    open fun getContentForChapter(chapId: Int): Flow<UiContent?> {
        return contentRepository?.getContentForChapter(chapId)
            ?.map { contentEntity -> contentEntity?.toUi() } ?:flowOf(null)
    }

}