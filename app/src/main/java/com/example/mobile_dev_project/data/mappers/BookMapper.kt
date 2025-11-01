package com.example.mobile_dev_project.data.mappers


import com.example.mobile_dev_project.data.UiBook
import java.util.Date
import com.example.mobile_dev_project.data.entity.Book as BookEntity

fun UiBook.toEntity(): BookEntity {
    val entity = BookEntity(
        BookTitle = this.title,
        BookCoverPath = this.coverPath ?: "",
        bookAdded = this.dateAdded.time
    )
    entity.lastAccessed = this.lastAccessed.time
    entity.bookId = this.bookId ?: 0
    return entity
}

fun BookEntity.toUi(): UiBook {
    val uiBook = UiBook(
        bookId = this.bookId,
        title = this.bookTitle ?: "",
        coverPath = this.bookCoverPath,
        chapters = emptyList()
    )
    uiBook.dateAdded = Date(this.bookAdded ?: System.currentTimeMillis())
    uiBook.lastAccessed = Date(this.lastAccessed ?: System.currentTimeMillis())
    return uiBook
}