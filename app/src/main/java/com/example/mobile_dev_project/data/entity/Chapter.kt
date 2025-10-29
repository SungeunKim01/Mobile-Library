package com.example.mobile_dev_project.data.entity




class Chapter {


    var chapterId: Int = 0


    var bookId: Int = 0


    var chapterName: String? = null


    var chapterOrder: Int = 0

    var contentId: String? = null

    constructor() {}

    constructor(bookId: Int, chapterName: String?, chapterOrder: Int, contentId: String?) {
        this.bookId = bookId
        this.chapterName = chapterName
        this.chapterOrder = chapterOrder
        this.contentId = contentId
    }
}