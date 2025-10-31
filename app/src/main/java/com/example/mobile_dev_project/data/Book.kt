package com.example.mobile_dev_project.data

import java.time.LocalDate
import java.util.Date

class Book {
    var chapters: List<Chapter>
    var title: String
    var dateAdded: Date
    var lastAccessed: Date

    constructor(title: String, chapters: List<Chapter>){
        this.title = title
        this.chapters = chapters
        this.dateAdded = Date(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
        this.lastAccessed = Date(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
    }

    fun accessedBook(){
        this.lastAccessed = Date(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
    }
}

val mockBook = Book("Hello", mockChapters)