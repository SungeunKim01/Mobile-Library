package com.example.mobile_dev_project.nav

import android.net.Uri

sealed class Route(val route: String) {
    data object Download : Route("download")
    data object Search : Route("search")
    data object Home : Route("home")
    data object Content: Route("content/{bookId}"){
        fun createRoute(bookId: Int): String = "content/$bookId"
    }
    data object Toc : Route("toc")

    data object Reading : Route("reading/{bookId}/{chapterId}/{scrollRatio}?query={query}") {
        fun createRoute(bookId: Int?, chapterId: Int?, scrollRatio: Float = -1f, query: String = ""):
        String {
            val encoded = Uri.encode(query)
            return "reading/$bookId/$chapterId/$scrollRatio?query=$encoded"
        }
    }
}
