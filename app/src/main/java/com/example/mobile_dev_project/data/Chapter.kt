package com.example.mobile_dev_project.data

class Chapter {
    var chaptertitle: String
    var content: Content

    constructor(chaptertitle: String, content: Content){
        this.chaptertitle = chaptertitle
        this.content = content
    }
}
var mockContent = Content("Mock Mock Mock")

val mockChapters = listOf(
    Chapter(
        "progtis riport 1",
        mockContent
    ),
    Chapter(
        "progris riport 2",
        mockContent
    ),
    Chapter(
        "progris riport 3",
        mockContent
    ),
    Chapter(
        "progris riport 4",
        mockContent
    ),
    Chapter(
        "progris riport 5",
        mockContent
    ),
    Chapter(
        "progris riport 6",
        mockContent
    ),
    Chapter(
        "progris riport 7",
        mockContent
    )
)
