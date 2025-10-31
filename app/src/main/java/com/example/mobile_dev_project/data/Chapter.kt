package com.example.mobile_dev_project.data

data class UiChapter (
    var chaptertitle: String,
    var content: UiContent
)
var mockContent = UiContent("Mock Mock Mock")

val mockChapters = listOf(
    UiChapter(
        "progtis riport 1",
        mockContent
    ),
    UiChapter(
        "progris riport 2",
        mockContent
    ),
    UiChapter(
        "progris riport 3",
        mockContent
    ),
    UiChapter(
        "progris riport 4",
        mockContent
    ),
    UiChapter(
        "progris riport 5",
        mockContent
    ),
    UiChapter(
        "progris riport 6",
        mockContent
    ),
    UiChapter(
        "progris riport 7",
        mockContent
    )
)
