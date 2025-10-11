package com.example.mobile_dev_project

import android.app.Activity
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mobile_dev_project.data.Chapter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource

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

    LazyRow(
        state = listState,
        //modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(chapters) { index, (title, text) ->
            ChapterPage(title, text, onSearch, onBack)
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
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.widthIn(max = 400.dp)
            ) {
                Button(onClick = onSearch) { Text(text = stringResource(R.string.search_btn)) }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Spacer(Modifier.height(12.dp))
                //val cleanContent = content.replace(Regex("\\s+"), " ").trim()
                Text(
                    text = content.trimIndent(),
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 26.sp
                )
            }
        }
        FloatingActionButton(onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Text(text =stringResource(R.string.back_btn))
        }
    }
}
