package com.example.mobile_dev_project.data

import java.time.LocalDate
import java.util.Date

data class Book (
    var chapters: List<UiChapter>,
    var title: String,
    var dateAdded: Date = Date(),
    var lastAccessed: Date = Date(),
) {
    fun accessedBook() {
        lastAccessed = Date()
    }
}

val mockBook = Book(mockChapters, "Hello")