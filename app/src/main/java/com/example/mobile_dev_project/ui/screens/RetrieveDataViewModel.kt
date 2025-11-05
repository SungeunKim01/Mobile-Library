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
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import com.example.mobile_dev_project.data.mappers.*
import com.example.mobile_dev_project.data.entity.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first

@HiltViewModel
class RetrieveDataViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {
    suspend fun getBookshelf() : List<UiBook>{
        return bookRepository.allBooks.first().map { it.toUi() }
    }
    suspend fun getChaptersForBook(bookId: Int): List<UiChapter>?{
        return chapterRepository.getChaptersForBook(bookId).first().map{ it.toUi()}
    }
    suspend fun getContentForChapter(chapId: Int): UiContent?{
        return contentRepository.getContentForChapter(chapId).first()?.toUi()
    }

}