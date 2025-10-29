package com.example.mobile_dev_project.data.entity

import java.util.Date

class Book {
    var BookId: Int = 0
    var BookTitle: String? = null
    var LastAccessed: Date? = null
    var DateAccessed: Date? = null
    var BookCoverPath: String? = null

    constructor() {}

    constructor(BookId: Int, BookTitlel: String, LastAccessed: Date, DateAdded: Date, BookCoverPath: String) {
        this.BookId = BookId
        this.BookTitle = BookTitle
        this.LastAccessed = LastAccessed
        this.DateAccessed = DateAccessed
        this.BookCoverPath = BookCoverPath
    }


}