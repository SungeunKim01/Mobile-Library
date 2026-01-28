package com.example.mobile_dev_project.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.example.mobile_dev_project.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobile_dev_project.data.NavScreen
import com.example.mobile_dev_project.nav.Route

/**
 * this is place to compose the app's top level ui Scaffold and load the NavHost
 * here, we can later branch on WindowSizeClass here to switch btw BottomBar /TopBar or Rail orDrawer
 */
@Composable
fun AppScaffold(
    nav: NavHostController,
) {
    var showBottomBar by remember { mutableStateOf(true) }

    var currentBookId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        // bottomBar = {},
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = nav, currentBookId = currentBookId)
            }
        }
        // topBar = {}
    ) { innerPadding ->
        AppNavHost(
            nav = nav,
            // here, change Route.Search.route or Route.Home.route or Route.Download.route more
            startDestination = Route.Home.route,
            modifier = Modifier.padding(innerPadding),
            onToggleNavBar = { show -> showBottomBar = show },
            onBookSelectedForToc = { bookId -> currentBookId = bookId }
        )
    }
}
//This code is taken from my previous lab BarCodeExample, I just tweaked it so its able to not take in icons but instead title and Route
@Composable
fun BottomNavigationBar(navController: NavHostController, currentBookId: Int?) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val homeLabel = stringResource(R.string.home)
        val searchLabel = stringResource(R.string.search)
        val tocLabel = stringResource(R.string.toc)
        val downloadLabel = stringResource(R.string.download)

        val items = listOf(
            NavScreen(stringResource(R.string.home), null, Route.Home.route),
            NavScreen(stringResource(R.string.search), null, Route.Search.route),
            NavScreen(tocLabel, null, "toc_tab"),
            NavScreen(stringResource(R.string.download), null, Route.Download.route)
        )

        items.forEach { navItem ->
            if (!navItem.route.contains("{")) {

                val isTocTab = navItem.title == tocLabel
                val isSelected =
                    if (isTocTab) {
                        currentRoute?.startsWith("content") == true
                    } else {
                        currentRoute == navItem.route
                    }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        // Home
                        if (navItem.title == homeLabel) {
                            navController.navigate(Route.Home.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = false
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                        // TOC, only if a book is opened
                        else if (navItem.title == tocLabel) {
                            val bookId = currentBookId
                            if (bookId != null) {
                                navController.navigate(
                                    Route.Content.createRoute(bookId)
                                ) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            //do nothing  if bookId is null
                        }
                        // Search and Download
                        else {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    // no icon
                    icon = { },
                    label = {
                        Text(text = navItem.title)
                    },
                )
            }
        }
    }
}