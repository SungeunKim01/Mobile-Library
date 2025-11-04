package com.example.mobile_dev_project.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UiBook (
    val bookId: Int? = null,
    val chapters: List<UiChapter> = emptyList(),
    val title: String,
    val coverPath: String? = null,
) {
    private val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    private val now = Date()

    var dateAdded: String = formatter.format(now)
    var lastAccessed: String = formatter.format(now)

    fun accessedBook() {
        lastAccessed = formatter.format(Date())
    }
}

val mockBook = UiBook(1, mockChapters, "Hello")