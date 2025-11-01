package com.example.mobile_dev_project.data

import java.time.LocalDate
import java.util.Date

data class UiBook (
    val bookId: Int? = null,
    val chapters: List<UiChapter> = emptyList(),
    val title: String,
    val coverPath: String? = null,
) {
    var dateAdded: Date = Date()
    var lastAccessed: Date = Date()

    fun accessedBook() {
        lastAccessed = Date()
    }
}

val mockBook = UiBook(1, mockChapters, "Hello")