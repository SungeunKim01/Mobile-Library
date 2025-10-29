package com.example.mobile_dev_project.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["bookId"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
class Chapter {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chapterId")
    var chapterId: Int = 0

    @ColumnInfo(name = "bookId")
    var bookId: Int = 0

    @ColumnInfo(name = "chapterName")
    var chapterName: String? = null

    @ColumnInfo(name = "chapterOrder")
    var chapterOrder: Int = 0

    @ColumnInfo(name = "contentId")
    var contentId: String? = null

    constructor() {}

    constructor(bookId: Int, chapterName: String?, chapterOrder: Int, contentId: String?) {
        this.bookId = bookId
        this.chapterName = chapterName
        this.chapterOrder = chapterOrder
        this.contentId = contentId
    }
}