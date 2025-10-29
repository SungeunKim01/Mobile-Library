package com.example.mobile_dev_project.data

class Book {
    var chapters: List<Chapter>
    var title: String

    constructor(title: String, chapters: List<Chapter>){
        this.title = title
        this.chapters = chapters
    }
}

val mockBook = Book("Hello", mockChapters)