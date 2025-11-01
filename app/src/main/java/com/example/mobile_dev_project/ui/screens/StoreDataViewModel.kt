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
            // Insert Book
            val bookId = bookRepository.insertBook(uiBook.toEntity())

            // Insert Chapter
            uiBook.chapters.forEachIndexed { index, uiChapter ->
                val chapterEntity = uiChapter.copy(
                    bookId = bookId.toInt(),
                    chapterOrder = index + 1
                ).toEntity()

                val chapterId = chapterRepository.insertChapter(chapterEntity)

                // Insert Content
                val content = uiContents.find { it.chapterId == uiChapter.chapterId }
                if (content != null) {
                    val contentEntity = content.copy(chapterId = chapterId.toInt()).toEntity()
                    contentRepository.insertContent(contentEntity)
                }
            }
        }
    }
}
