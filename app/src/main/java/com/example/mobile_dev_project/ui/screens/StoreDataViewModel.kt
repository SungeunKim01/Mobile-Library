package com.example.mobile_dev_project.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.mappers.toEntity
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import com.example.mobile_dev_project.data.repository.ParsingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreDataViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository,
    private val parsingRepository: ParsingRepository
) : ViewModel() {

    fun storeBookData(uiBook: UiBook) {
        viewModelScope.launch {

            //note: checking if book exists before inserting!
            val existingBook = bookRepository.bookExists(uiBook.title)
            if (existingBook) {
                Log.e("StoreDataViewModel","Book already exists. No duplicates allowed.")
                return@launch
            }

            // Insert Book
            val bookId = bookRepository.insertBook(uiBook.toEntity())
            val (parsedBook, parsedContents) = parsingRepository.parseHtml(bookId.toString())

            // Insert Chapter
            for(i in parsedBook.chapters.indices){
                val chap = parsedBook.chapters[i]
                val content = parsedContents.getOrNull(i) ?: continue
                val chapterEntity = chap.copy(
                    bookId = bookId.toInt()
                ).toEntity()
                val chapterId = chapterRepository.insertChapter(chapterEntity)
                val contentEntity = content.copy(chapterId = chapterId.toInt()).toEntity()
                contentRepository.insertContent(contentEntity)
            }

        }
    }
}
