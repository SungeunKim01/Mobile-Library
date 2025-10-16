package com.example.mobile_dev_project.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_dev_project.nav.Route
import com.example.mobile_dev_project.ui.screens.DownloadBookScreen
import com.example.mobile_dev_project.ui.screens.SearchScreen
import com.example.mobile_dev_project.ui.screens.HomeScreen
import com.example.mobile_dev_project.ui.screens.TableOfContentsScreen
import com.example.mobile_dev_project.ui.screens.ReadingScreen
import com.example.mobile_dev_project.data.mockChapters
import androidx.navigation.NavGraph.Companion.findStartDestination


/**
 * this is centralized NavHost for the app
 * I'll put all my routes here so TopBar scaffolds can reuse it
 */

// helper that pops if possible, otherwise navigates to Home as singleTop
private fun NavHostController.safePopOrNavigateHome() {
    val didPop = popBackStack()
    if (!didPop) {
        navigate(Route.Home.route) {
            popUpTo(graph.findStartDestination().id) { inclusive = false }
            launchSingleTop = true
        }
    }
}

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
                onBack = { nav.safePopOrNavigateHome() }
            )
        }

        // Search Screen -UI only for now for m1
        composable(Route.Search.route) {
            SearchScreen(
                onBack = { nav.safePopOrNavigateHome() }
            )
        }

        // Add other screens like Home, we will merge this file after we finish all the screens
        composable(Route.Home.route) {
            HomeScreen(
                onNavigateToDownload = { nav.navigate(Route.Download.route)  }
            )
        }

        composable(Route.Content.route){
            TableOfContentsScreen(
                onBack = { nav.popBackStack() }
            )
        }
        composable(Route.Reading.route) {
            ReadingScreen(
                chapters = mockChapters,
                //later, this will change depending on which chapter user clicks.
                chapterIndexSelected = 0,
                onSearch = { nav.navigate(Route.Search.route) },
                onBack = { nav.popBackStack() }
            )
        }
    }
}