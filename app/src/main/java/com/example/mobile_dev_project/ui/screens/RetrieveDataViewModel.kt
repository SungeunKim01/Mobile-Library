package com.example.mobile_dev_project.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.repository.*
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.example.mobile_dev_project.data.mappers.*
import com.example.mobile_dev_project.data.entity.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@HiltViewModel
class RetrieveDataViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {
    fun getBookshelf(): Flow<List<UiBook>> {
        return bookRepository.allBooks.map { books ->
            books.map { it.toUi() }
        }
    }
    fun getChaptersForBook(bookId: Int): Flow<List<UiChapter>> {
        return chapterRepository.getChaptersForBook(bookId)
            .map { chapters -> chapters.map { it.toUi() } }
    }
    fun getContentForChapter(chapId: Int): Flow<UiContent?> {
        return contentRepository.getContentForChapter(chapId)
            .map { contentEntity -> contentEntity?.toUi() }
    }

}