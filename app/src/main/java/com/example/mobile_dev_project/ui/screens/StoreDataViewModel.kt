package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.mappers.toEntity
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreDataViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    fun storeBookData(uiBook: UiBook, uiContents: List<UiContent>) {
        viewModelScope.launch {
            // 1️⃣ Insert book
            val bookId = bookRepository.insertBook(uiBook.toEntity()).toInt()

            // 2️⃣ Insert contents (save all contents first)
            uiContents.forEach { uiContent ->
                contentRepository.insertContent(uiContent.toEntity(uiContent.chapterId))
            }

            // 3️⃣ Insert chapters (they already have contentIds)
            uiBook.chapters.forEachIndexed { index, uiChapter ->
                val chapterEntity = uiChapter.toEntity(bookId)
                chapterEntity.chapterOrder = index + 1
                chapterRepository.insertChapter(chapterEntity)
            }
        }
    }
}
