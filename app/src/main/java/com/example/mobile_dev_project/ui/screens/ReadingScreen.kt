package com.example.mobile_dev_project.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_dev_project.data.UiChapter as Chapter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mobile_dev_project.R
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.UiChapter
import kotlinx.coroutines.flow.first
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon

/**
 * Sets up the immersive mode and handles displaying the entire screen
 * Get data from table of contents, fetch content from chapters from view model.
 * general sources: - Week 12: Room Database slide 63
 *                  - https://developer.android.com/develop/ui/views/layout/immersive
 *                  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/map-not-null.html
 */
@Composable
fun ReadingScreen (bookId: Int,
                   chapterId: Int,
                   onSearch: () -> Unit,
                   onBack: () -> Unit,
                   onToggleNavBar: (Boolean) -> Unit = {},
                   viewModel: RetrieveDataViewModel = hiltViewModel()){
    val view = LocalView.current
    val window = (view.context as Activity).window
    // Create a controller to show/hide system bars
    val windowInsetsController = remember {
        WindowCompat.getInsetsController(window, view)
    }
    // Mutable state that tracks whether the screen is in immersive mode
    var isImmersive by remember { mutableStateOf(false) }
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
    var chapters by remember { mutableStateOf<List<UiChapter>>(emptyList()) }
    var contents by remember { mutableStateOf<List<UiContent>>(emptyList()) }
    var selectedIndex by remember { mutableStateOf(0) }
    val allChaps by viewModel.getChaptersForBook(bookId).collectAsState(initial = emptyList())
    LaunchedEffect(allChaps) {
        val allContents = allChaps.mapNotNull {
            val content = viewModel.getContentForChapter(it.chapterId ?: 0).first()
            content
        }
        chapters = allChaps
        contents = allContents
        selectedIndex = allChaps.indexOfFirst { it.chapterId == chapterId }
    }
    if (chapters.isEmpty() || contents.isEmpty()) {
        LoadingIndicator()
    } else {
        var isVisible by remember { mutableStateOf(false) }
        val localView = LocalView.current
        val window = (localView.context as Activity).window
        val windowInsetsController = remember {
            WindowCompat.getInsetsController(window, localView)
        }
        Box(modifier = Modifier.clickable { toggleImmersiveMode() }){
            ReadingPageContent(chapters = chapters, contents = contents, chapterIndexSelected = selectedIndex, onSearch = onSearch, onBack = onBack)
            if (isImmersive) {
                Text(
                    text = stringResource(R.string.tap_anywhere_to_exit_fullscreen),
                    modifier = Modifier
                        .padding(8.dp)
                        .testTag("fullscreen_text")
                )
            }
        }
    }
}

/**
 * Shows circle that indicates loading
 * src: //https://developer.android.com/reference/com/google/android/material/progressindicator/CircularProgressIndicator
 */
@Composable
private fun LoadingIndicator() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Displays content of the book and handles horizontal/vertical scrolling.
 */
//https://developer.android.com/develop/ui/compose/lists
@Composable
fun ReadingPageContent(
    chapters: List<Chapter>,
    contents: List<UiContent>,
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
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(chapters) { _, chapter ->
            val contentText = contents.find { it.chapterId == chapter.chapterId }?.content ?: ""
            chapter.contentId?.let {
                ChapterPage(
                    title = chapter.chapterTitle,
                    content = contentText,
                    contentId = it,
                    onSearch = onSearch,
                    onBack = onBack,
                    )
            }
        }
    }
}


/**
 * Displays a single chapter of the book.
 * Display search and back button.
 * scrolling: https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary#rememberScrollState(kotlin.Int)
 */

//for the floating action btn
//https://developer.android.com/develop/ui/compose/components/fab
@Composable
fun ChapterPage(
    title: String,
    content: String,
    contentId: Int,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    viewModel: PositionViewModel = hiltViewModel()
) {
    val state = rememberScrollState()
    LaunchedEffect(contentId) {
        viewModel.getScrollPosition(contentId)?.let { saved ->
            state.scrollTo(saved.toInt())
        }
    }
    LaunchedEffect(state.value) {
        viewModel.saveScrollPosition(contentId, state.value.toFloat())
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.padding_reg), vertical = dimensionResource(R.dimen.space_xxl)).verticalScroll(state)
        ) {
            SearchButton(onSearch)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_reg)))
            ChapterTitle(title)
            ChapterContent(content)
        }
        FloatingActionButton(onClick = onBack,
            modifier = Modifier
                .padding(bottom= dimensionResource(R.dimen.space_xxl), end= dimensionResource(R.dimen.space_lg)).align(Alignment.BottomEnd).testTag("back_btn"),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = dimensionResource(R.dimen.elevation_med))
        ) {
            Text(text = stringResource(R.string.back_btn), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ChapterTitle(title: String){
    Text(text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        lineHeight = dimensionResource(R.dimen.line_height_reg).value.sp,
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_reg)).testTag("title"),
        textAlign = TextAlign.Center
    )
}
/**
 * Displays the content of a chapter.
 */
@Composable
fun ChapterContent(content : String, modifier: Modifier = Modifier){
    Column(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - dimensionResource(R.dimen.space_lg))){
        Text(text = content,
            modifier = Modifier.fillMaxWidth().testTag("content"),
            lineHeight = dimensionResource(R.dimen.line_height_reg).value.sp
        )
    }
}

/**
 * Displays the search button.
 */
@Composable
fun SearchButton(onSearch: () -> Unit, modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            tonalElevation = dimensionResource(R.dimen.elevation_high),
            shape = MaterialTheme.shapes.medium
        ) {
            Button(
                onClick = onSearch,
                modifier = modifier.testTag("search_btn")
            ) {
                Text(text = stringResource(R.string.search_btn))
            }
        }
    }

}


/**
 * Display the control bar for TTS.
 * Features: pause, play, stop.
 * For now, since VM not implemented, i just put a random vm.
 */
@Composable
fun TTSControlBar(viewModel: StoreDataViewModel , chapterContent: UiContent, onToggleNavBar: (Boolean) -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)

    ){
        val view = LocalView.current
        val window = (view.context as Activity).window
        val windowInsetsController = remember {
            WindowCompat.getInsetsController(window, view)
        }
        var isImmersive by remember { mutableStateOf(false) }
        fun toggleImmersiveMode() {
            isImmersive = !isImmersive
            if (isImmersive) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                // Show system bars (exit fullscreen)
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }
            onToggleNavBar(!isImmersive)
        }
        Button(onClick = {}){ // when we get viewmodel, i edit this
            Icon(Icons.Default.Stop, contentDescription = "Stop TTS.")
        }
        Spacer(modifier = Modifier.width(16.dp))

        var isTTSEnabled = true
        Button(onClick = {
            if(isTTSEnabled){
                //stop the tts
                isTTSEnabled = false
            } else {
                isTTSEnabled = true
            }
        }) {
            val icon = if (isTTSEnabled) Icons.Default.Pause else Icons.Default.PlayArrow
            Icon(icon, contentDescription = if(isTTSEnabled) "Pause TTS" else "Play TTS")
        }
    }
}
/**
 * Composable used for testing
 */
@Composable
fun ReadingScreenForTest(
    chapters: List<UiChapter>,
    contents: List<UiContent>,
    chapterIndexSelected: Int = 0,
    onSearch: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    ReadingPageContent(
        chapters = chapters,
        contents = contents,
        chapterIndexSelected = chapterIndexSelected,
        onSearch = onSearch,
        onBack = onBack
    )
}


