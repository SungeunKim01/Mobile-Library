package com.example.mobile_dev_project.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mobile_dev_project.nav.Route

/**
 * this is place to compose the app's top level ui Scaffold and load the NavHost
 * here, we can later branch on WindowSizeClass here to switch btw BottomBar /TopBar or Rail orDrawer
 */
@Composable
fun AppScaffold(
    nav: NavHostController,
) {
    Scaffold(
        // bottomBar = {},
        // topBar = {}
    ) { innerPadding ->
        AppNavHost(
            nav = nav,
            // here, change Route.Search.route or Route.Home.route or Route.Download.route more
            startDestination = Route.Search.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
