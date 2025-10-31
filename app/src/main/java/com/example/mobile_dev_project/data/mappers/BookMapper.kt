package com.example.mobile_dev_project.data.mappers


import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.entity.Book as BookEntity

fun UiBook.toEntity(): BookEntity {
    val entity = BookEntity(
        BookTitle = this.title,
        BookCoverPath = this.coverPath ?: "",
        bookAdded = this.dateAdded.time
    )
    entity.lastAccessed = this.lastAccessed.time
    return entity
}

fun BookEntity.toUi(): UiBook {
    val uiBook = UiBook(
        title = this.bookTitle ?: "",
        chapters = emptyList() 
    )
    uiBook.dateAdded = java.util.Date(this.bookAdded ?: System.currentTimeMillis())
    uiBook.lastAccessed = java.util.Date(this.lastAccessed ?: System.currentTimeMillis())
    return uiBook
}