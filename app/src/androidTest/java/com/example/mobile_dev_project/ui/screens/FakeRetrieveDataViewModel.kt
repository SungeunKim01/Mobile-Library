package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRetrieveDataViewModel(
    private val chapters: List<UiChapter> = emptyList(),
    private val contents: List<UiContent> = emptyList()
) : RetrieveDataViewModel(null, null, null) {

    override fun getChaptersForBook(bookId: Int): Flow<List<UiChapter>> {
        return flowOf(chapters)
    }

    override fun getContentForChapter(chapId: Int): Flow<UiContent?> {
        return flowOf(contents.find { it.chapterId == chapId })
    }

    override fun getBookshelf(): Flow<List<UiBook>> {
        return flowOf(emptyList())
    }
}
