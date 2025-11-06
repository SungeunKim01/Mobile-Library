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
    Scaffold(
        // bottomBar = {},
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = nav)
            }
        }
        // topBar = {}
    ) { innerPadding ->
        AppNavHost(
            nav = nav,
            // here, change Route.Search.route or Route.Home.route or Route.Download.route more
            startDestination = Route.Home.route,
            modifier = Modifier.padding(innerPadding),
            onToggleNavBar = { show -> showBottomBar = show }
        )
    }
}
//This code is taken from my previous lab BarCodeExample, I just tweaked it so its able to not take in icons but instead title and Route
@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        val items = listOf(
            NavScreen(stringResource(R.string.home), null, Route.Home.route),
            NavScreen(stringResource(R.string.table), null, Route.Content.route),
            NavScreen(stringResource(R.string.search), null, Route.Search.route)
        )

        items.forEach { navItem ->

            NavigationBarItem(

                selected = currentRoute == 	navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {

                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    //empty cause I dont want an icon for them
                },
                label = {
                    //We are going to use instead of icons the second part so H, T, and S each meaning a different screen
                    Text(text = navItem.title)
                },
            )
        }
    }
}