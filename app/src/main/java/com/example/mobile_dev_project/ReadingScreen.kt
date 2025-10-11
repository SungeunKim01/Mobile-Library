package com.example.mobile_dev_project

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight


//pairs: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-pair/
@Composable
fun ReadingScreen (chapters: List<Pair<String, String>>,
                   chapterIndexSelected: Int,
                   onSearch: () -> Unit){
    var currentChapterIndex by remember {mutableStateOf(chapterIndexSelected)}
    ReadingPageContent(
        chapters = chapters,
        chapterIndexSelected = chapterIndexSelected,
        onSearch = onSearch
    )

}


//https://developer.android.com/develop/ui/compose/lists
@Composable
fun ReadingPageContent(
    chapters: List<Pair<String, String>>,
    chapterIndexSelected: Int,
    onSearch: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(chapterIndexSelected) {
        listState.scrollToItem(chapterIndexSelected)
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(chapters) { index, (title, text) ->
            ChapterPage(title, text, onSearch)
        }
    }
}

@Composable
fun ChapterPage(
    title: String,
    content: String,
    onSearch: () -> Unit
) {
    Button(onClick = onSearch) { Text("Search")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(text = content, fontSize = 18.sp)
    }
}

