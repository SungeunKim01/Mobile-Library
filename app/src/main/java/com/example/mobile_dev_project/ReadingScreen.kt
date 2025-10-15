package com.example.mobile_dev_project

import android.app.Activity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mobile_dev_project.data.Chapter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign

//pairs: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-pair/
@Composable
fun ReadingScreen (chapters: List<Chapter>,
                   chapterIndexSelected: Int,
                   onSearch: () -> Unit,
                   onBack: () -> Unit){
    var currentChapterIndex by remember {mutableStateOf(chapterIndexSelected + 1)}
    var isVisible by remember { mutableStateOf(true) }
    val localView = LocalView.current
    val window = (localView.context as? Activity)?.window
    //tap is deprecated so............ !
    val windowInsetsController = remember {
        window?.let { w ->
            WindowCompat.getInsetsController(w, w.decorView).apply {
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
    ReadingPageContent(
        chapters = chapters,
        chapterIndexSelected = currentChapterIndex,
        onSearch = onSearch,
        onBack = onBack,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures {
                windowInsetsController?.let { controller ->
                    if (isVisible) {
                        controller.hide(WindowInsetsCompat.Type.systemBars())
                    } else {
                        controller.show(WindowInsetsCompat.Type.systemBars())
                    }
                    isVisible = !isVisible
                }
            }
        }
    )
}


//https://developer.android.com/develop/ui/compose/lists
@Composable
fun ReadingPageContent(
    chapters: List<Chapter>,
    chapterIndexSelected: Int,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    modifier : Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LaunchedEffect(chapterIndexSelected) {
        listState.scrollToItem(chapterIndexSelected)
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.Center
        ) {
            itemsIndexed(chapters) { index, (title, text) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    ChapterPage(title, text, onSearch, onBack)
                }
            }
        }
    }
}

//for the floating action btn
//https://developer.android.com/develop/ui/compose/components/fab
@Composable
fun ChapterPage(
    title: String,
    content: String,
    onSearch: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 60.dp).verticalScroll(rememberScrollState())
        ) {
            SearchButton(onSearch)
            Spacer(Modifier.height(16.dp))
            Text(text = title,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            ChapterContent(content)
        }
        FloatingActionButton(onClick = onBack,
            modifier = Modifier
                .padding(24.dp).align(Alignment.BottomEnd),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Text(text = stringResource(R.string.back_btn), fontSize = 20.sp)
        }
    }
}

@Composable
fun ChapterContent(content : String){
    Column(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 20.dp)){
        Text(text = content,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 26.sp
        )
    }
}


@Composable
fun SearchButton(onSearch: () -> Unit, modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Button(
                onClick = onSearch,
                modifier = modifier
            ) {
                Text(text = stringResource(R.string.search_btn))
            }
        }
    }

}
