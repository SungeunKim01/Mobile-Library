package com.example.mobile_dev_project.nav

sealed class Route(val route: String) {
    data object Download : Route("download")
    data object Search   : Route("search")
    data object Home : Route("Home")
    data object Content: Route("Content/{bookId}"){
        fun createRoute(bookId: Int): String = "Content/$bookId"
    }
    data object Reading: Route("reading/{chapterIndex}"){
        fun createRoute(chapterIndex: Int): String = "reading/$chapterIndex"
    }
}
