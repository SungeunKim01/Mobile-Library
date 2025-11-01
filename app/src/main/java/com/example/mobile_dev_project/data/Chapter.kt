package com.example.mobile_dev_project.data

data class UiChapter (
    val chapterId: Int? = null,
    val chapterTitle: String,
    val chapterOrder: Int,
    val bookId: Int,
    val contentId: Int? = null
)
var mockContent = UiContent(1, 1, "Mock Mock Mock")

val mockChapters = listOf(
    UiChapter(
        1,
        "progtis riport 1",
        1,
        1,
        1
    ),
    UiChapter(
        2,
        "progris riport 2",
        2,
        1,
        1,
    ),
    UiChapter(
        3,
        "progris riport 3",
        3,
        1,
        1
    ),
    UiChapter(
        4,
        "progris riport 4",
        4,
        1,
        1
    ),
    UiChapter(
        5,
        "progris riport 5",
        5,
        1,
        1
    ),
    UiChapter(
        6,
        "progris riport 6",
        6,
        1,
        1
    ),
    UiChapter(
        7,
        "progris riport 7",
        7,
        1,
        1
    )
)
