package com.example.mobile_dev_project.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mobile_dev_project.R

@Composable
fun TableOfContentsScreen(
    chapters: List<String> = listOf("Chapter 1: The Beginning", "Chapter 2: The Journey", "Chapter 3: The End"),
    onChapterSelected: (String) -> Unit = {}
){
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as android.app.Activity).window
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
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.table_of_contents),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(chapters) { chapter ->
                    Button(
                        onClick = { onChapterSelected(chapter) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = chapter)
                    }
                }
            }
        }


    }

}