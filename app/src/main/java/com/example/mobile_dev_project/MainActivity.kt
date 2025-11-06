package com.example.mobile_dev_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.mobile_dev_project.ui.AppScaffold
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.mobile_dev_project.ui.screens.ImportViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val importViewModel: ImportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            // Call importInitialBooks() so this will
            // load urls from strings.xml then
            // Check database for duplicates then
            // download, unzip, parse, and save each book if not alr present
            importViewModel.importInitialBooks { progressState ->
                Log.d("MainActivity", "Import Progress: ${progressState.phase} - ${progressState.message}")
                progressState.detail?.let {
                    Log.d("MainActivity", "  Detail: $it")
                }
            }
        }

        setContent {
            MobileDevProjectTheme {
                val nav = rememberNavController()
                AppScaffold(nav = nav)
            }
        }
    }
}