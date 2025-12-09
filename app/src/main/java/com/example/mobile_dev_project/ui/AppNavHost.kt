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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mobile_dev_project.ui.screens.PositionViewModel
import com.example.mobile_dev_project.ui.screens.RetrieveDataViewModel

import com.example.mobile_dev_project.ui.screens.TTsViewModel

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
    modifier: Modifier = Modifier,
    onToggleNavBar: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = nav,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Download Book Screen - UI only for now for m1
        composable(Route.Download.route) {
            DownloadBookScreen(
                onBack = { nav.safePopOrNavigateHome() },
                onToggleNavBar = onToggleNavBar
            )
        }

        // Search Screen -UI only for now for m1
        composable(Route.Search.route) {
            SearchScreen(
                onBack = { nav.safePopOrNavigateHome() },
                onNavigateToLocation = { hit ->
                    // go to the proper chapter in ReadingScreen wen user taps a result
                    nav.navigate(
                        Route.Reading.createRoute(
                            hit.bookId,
                            hit.chapterId,
                            hit.scrollRatio,
                            hit.query
                        )
                    )
                },
                onToggleNavBar = onToggleNavBar
            )
        }

        // Add other screens like Home, we will merge this file after we finish all the screens
        composable(Route.Home.route) {
            HomeScreen(
                onNavigateToDownload = { nav.navigate(Route.Download.route)  },
                onNavigateToContents = { bookId ->
                    nav.navigate(Route.Content.createRoute(bookId))
                },
                onToggleNavBar = onToggleNavBar
            )
        }

        composable(
            route = Route.Content.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0

            TableOfContentsScreen(
                bookId = bookId,
                onBack = { nav.popBackStack() },
                onChapterSelected = { chapter ->
                    nav.navigate(Route.Reading.createRoute(bookId, chapter.chapterId ?:0))
                },
                onToggleNavBar = onToggleNavBar
            )
        }

        composable(Route.Reading.route,
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.IntType
                },
                navArgument("chapterId") {
                    type = NavType.IntType
                },
                navArgument("scrollRatio") {
                    type = NavType.FloatType
                    defaultValue = -1f
                },
                navArgument("query") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?:0
            val chapId = backStackEntry.arguments?.getInt("chapterId") ?:0


            val ttsViewModel = hiltViewModel<TTsViewModel>()

            ttsViewModel.prepareChapterById(chapId)
            val posVM = hiltViewModel<PositionViewModel>()
            val retrieveVM = hiltViewModel<RetrieveDataViewModel>()

            val scrollRatio = backStackEntry.arguments?.getFloat("scrollRatio") ?:-1f
            val query = backStackEntry.arguments?.getString("query") ?: ""


            ReadingScreen(
                bookId = bookId,
                chapterId = chapId,
                onSearch = { nav.navigate(Route.Search.route) },
                onBack = { nav.popBackStack() },
                initialScrollRatio = scrollRatio,
                searchQuery = query,
                onToggleNavBar = onToggleNavBar,
                ttsVM = ttsViewModel,
                positionVM = posVM,
                retrieveVM = retrieveVM
            )
        }
    }
}