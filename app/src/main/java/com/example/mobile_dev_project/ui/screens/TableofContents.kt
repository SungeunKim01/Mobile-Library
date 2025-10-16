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
import com.example.mobile_dev_project.R

@Composable
fun TableOfContentsScreen(
    chapters: List<String> = listOf("Chapter 1: The Beginning", "Chapter 2: The Journey", "Chapter 3: The End"),
    onChapterSelected: (String) -> Unit = {},
    onBack: () -> Unit
){
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as Activity).window
    val windowInsetsController = remember {
        WindowCompat.getInsetsController(window, view)
    }

    var isImmersive by remember { mutableStateOf(false) }

    fun toggleImmersiveMode(){
        isImmersive = !isImmersive
        if(isImmersive){
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
        else{
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { toggleImmersiveMode() }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .testTag("toc_box")
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {

            Text(
                text = stringResource(R.string.table_of_contents),
                modifier = Modifier.padding(bottom = 12.dp)
                    .testTag("toc_title")

            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("toc_list")
            ) {
                items(chapters) { chapter ->
                    Button(
                        onClick = { onChapterSelected(chapter) },
                        modifier = Modifier.fillMaxWidth()
                            .testTag("chapter_button_$chapter")
                    ) {
                        Text(text = chapter)
                    }
                }
            }

        }

        FloatingActionButton(onClick = onBack,
            modifier = Modifier
                .padding(bottom= 64.dp, end=24.dp)
                .align(Alignment.BottomEnd),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Text(text = stringResource(R.string.back_btn), fontSize = 20.sp)
        }

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