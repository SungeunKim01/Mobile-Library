package com.example.mobile_dev_project.data.entity

import androidx.room.*
@Entity(
    tableName="Book",
    indices = [
        // dedupe by url, here Room will reject any 2nd row w the same url
        Index(value = ["sourceUrl"], unique = true)
    ]
)
class Book {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookId")
    var bookId: Int? = 0
//the date should update every 1 minute
    @ColumnInfo(name="bookTitle")
    var bookTitle: String? = null

    @ColumnInfo(name = "bookCoverPath")
    var bookCoverPath: String? = null

    @ColumnInfo(name= "lastAccessed")
    var lastAccessed: String? = null
    @ColumnInfo(name = "bookAdded")
    var bookAdded: String? = null

    //dedupe field -unique
    @ColumnInfo(name = "sourceUrl")
    lateinit var sourceUrl: String

    constructor() {}

    constructor(BookTitle: String, BookCoverPath: String, bookAdded: String, sourceUrl: String) {
        this.bookTitle = BookTitle
        this.bookCoverPath = BookCoverPath
        this.bookAdded = bookAdded
        this.sourceUrl = sourceUrl
    }
}

