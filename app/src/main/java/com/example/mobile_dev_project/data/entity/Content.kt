package com.example.mobile_dev_project.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "contents",
    foreignKeys = [ForeignKey(
        entity = Chapter::class,
        parentColumns = ["chapterId"],
        childColumns = ["chapterId"],
        onDelete = ForeignKey.CASCADE
    )]
)
class Content {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contentId")
    var contentId: Int = 0

    @ColumnInfo(name = "chapterId")
    var chapterId: Int = 0

    @ColumnInfo(name = "content")
    var content: String? = null

    constructor() {}

    constructor(chapterId: Int, content: String?) {
        this.chapterId = chapterId
        this.content = content
    }
}