package com.example.mobile_dev_project.data.mappers

import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.entity.Chapter as ChapterEntity

fun UiChapter.toEntity(): ChapterEntity {
    val entity = ChapterEntity(
        bookId = this.bookId,
        chapterName = this.chapterTitle,
        chapterOrder = this.chapterOrder,
        contentId = this.contentId ?: 0
    )
    entity.chapterId = this.chapterId ?: 0
    return entity
}

fun ChapterEntity.toUi(): UiChapter {
    return UiChapter(
        chapterId = this.chapterId,
        chapterTitle = this.chapterName ?: "",
        chapterOrder = this.chapterOrder,
        bookId = this.bookId,
        contentId = this.contentId
    )
}