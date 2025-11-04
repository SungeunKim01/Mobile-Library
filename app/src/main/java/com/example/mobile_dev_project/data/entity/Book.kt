package com.example.mobile_dev_project.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="Book")
class Book {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookId")
    var bookId: Int = 0
//the date should update every 1 minute
    @ColumnInfo(name="bookTitle")
    var bookTitle: String? = null

    @ColumnInfo(name = "bookCoverPath")
    var bookCoverPath: String? = null

    @ColumnInfo(name= "lastAccessed")
    var lastAccessed: String? = null
    @ColumnInfo(name = "bookAdded")
    var bookAdded: String? = null


    constructor() {}

    constructor(BookTitle: String, BookCoverPath: String, bookAdded: String) {
        this.bookTitle = BookTitle
        this.bookCoverPath = BookCoverPath
        this.bookAdded = bookAdded
    }
}

