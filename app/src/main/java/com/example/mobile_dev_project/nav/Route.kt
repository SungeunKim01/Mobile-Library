package com.example.mobile_dev_project.nav

sealed class Route(val route: String) {
    data object Download : Route("download")
    data object Search   : Route("search")
}
