package com.example.mobile_dev_project.data.mappers

import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.entity.Chapter as ChapterEntity

fun UiChapter.toEntity(bookId: Int): ChapterEntity {
    val entity = ChapterEntity(
        bookId = bookId,
        chapterName = this.chaptertitle,
        chapterOrder = 0, // assign order later
        contentId = null  // contentId will be set after content is saved
    )
    return entity
}

fun ChapterEntity.toUi(content: UiContent? = null): UiChapter {
    val uiChapter = UiChapter(
        chaptertitle = this.chapterName ?: "",
        content = content ?: UiContent("")
    )
    return uiChapter
}