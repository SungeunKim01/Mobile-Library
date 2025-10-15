package com.example.mobile_dev_project.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_dev_project.nav.Route
import com.example.mobile_dev_project.ui.screens.DownloadBookScreen
import com.example.mobile_dev_project.ui.screens.SearchScreen

/**
 * this is centralized NavHost for the app
 * I'll put all my routes here so BottomBar / Rail / Drawer scaffolds can reuse it
 */
@Composable
fun AppNavHost(
    nav: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = nav,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Download Book Screen - UI only for now for m1
        composable(Route.Download.route) {
            DownloadBookScreen(
                onBack = { nav.popBackStack() }
            )
        }

        // Search Screen -UI only for now for m1
        composable(Route.Search.route) {
            SearchScreen(
                onBack = { nav.popBackStack() }
            )
        }

        // Add other screens like Home, we will merge this file after we finish all the screens
    }
}