package com.example.mobile_dev_project.data.mappers


import com.example.mobile_dev_project.data.UiBook
import java.util.Date
import com.example.mobile_dev_project.data.entity.Book as BookEntity

fun UiBook.toEntity(sourceUrl: String): BookEntity {
    val entity = BookEntity(
        BookTitle = this.title,
        BookCoverPath = this.coverPath ?: "",
        bookAdded = this.dateAdded,
        sourceUrl = sourceUrl
    )
    entity.lastAccessed = this.lastAccessed
    entity.bookId = this.bookId ?: 0
    return entity
}

fun BookEntity.toUi(): UiBook {
    return UiBook(
        bookId = this.bookId,
        title = this.bookTitle ?: "",
        coverPath = this.bookCoverPath,
        chapters = emptyList()
    ).apply {
        dateAdded = this@toUi.bookAdded ?: ""
        lastAccessed = this@toUi.lastAccessed ?: ""
    }
}