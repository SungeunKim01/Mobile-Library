package com.example.mobile_dev_project.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mobile_dev_project.R
import com.example.mobile_dev_project.data.mockChapters
import com.example.mobile_dev_project.data.UiChapter as Chapter

@Composable
fun TableOfContentsScreen(
    bookId: Int,
    viewModel: RetrieveDataViewModel = hiltViewModel(),
    onChapterSelected: (Chapter) -> Unit = {},
    //Callback that is used to go back to the previous page
    onBack: () -> Unit,
    //Callback for hiding
    onToggleNavBar: (Boolean) -> Unit = {}
)
{
    // --- Context and window setup for immersive mode ---
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as Activity).window

    // Create a controller to show/hide system bars
    val windowInsetsController = remember {
        WindowCompat.getInsetsController(window, view)
    }

    // Mutable state that tracks whether the screen is in immersive mode
    var isImmersive by remember { mutableStateOf(false) }

    // Function to toggle immersive mode on and off
    fun toggleImmersiveMode() {
        isImmersive = !isImmersive
        if (isImmersive) {
            // Hide system bars (enter fullscreen)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            // Show system bars (exit fullscreen)
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
        onToggleNavBar(!isImmersive)
    }

    val chapters by viewModel.getChaptersForBook(bookId).collectAsState(initial = emptyList())

    // --- Main screen layout ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Clicking anywhere toggles immersive mode
            .clickable { toggleImmersiveMode() }
            .padding(16.dp)
            // Apply a background color from the current Material theme
            .background(MaterialTheme.colorScheme.secondary)
            // Test tag for UI testing
            .testTag("toc_box")
    ) {
        // Column holds the title and list of chapters
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            // --- Title text ---
            Text(
                text = stringResource(R.string.table_of_contents),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("toc_title")
            )

            // --- Scrollable list of chapters ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("toc_list")
            ) {
                items(chapters) { chapter ->
                    // Each chapter is displayed as a button
                    Button(
                        onClick = { onChapterSelected(chapter) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("chapter_button_$chapter.chapter_id")
                    ) {
                        Text(text = chapter.chapterTitle)
                    }
                }
            }
        }

        // --- Floating Action Button for going back ---
        FloatingActionButton(
            onClick = onBack,
            modifier = Modifier
                .padding(bottom = 64.dp, end = 24.dp)
                .align(Alignment.BottomEnd)
                .testTag("back_button"),

            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            // The back button text (large font for visibility)
            Text(
                text = stringResource(R.string.back_btn),
                fontSize = 20.sp
            )
        }

        // --- Message shown only when immersive mode is active ---
        if (isImmersive) {
            Text(
                text = stringResource(R.string.tap_anywhere_to_exit_fullscreen),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .testTag("fullscreen_text")
            )
        }
    }
}
