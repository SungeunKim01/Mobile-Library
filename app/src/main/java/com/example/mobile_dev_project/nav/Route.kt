package com.example.mobile_dev_project.nav

sealed class Route(val route: String) {
    data object Download : Route("download")
    data object Search : Route("search")
    data object Home : Route("home")
    data object Content: Route("content/{bookId}"){
        fun createRoute(bookId: Int): String = "content/$bookId"
    }

    data object Reading : Route("reading/{bookId}/{chapterId}/{scrollRatio}") {
        fun createRoute(
            bookId: Int,
            chapterId: Int,
            scrollRatio: Float = -1f
        ): String = "reading/$bookId/$chapterId/$scrollRatio"
    }
}
