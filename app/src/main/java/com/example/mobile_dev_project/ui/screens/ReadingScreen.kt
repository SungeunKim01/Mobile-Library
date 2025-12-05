package com.example.mobile_dev_project.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import javax.inject.Inject
//import com.example.mobile_dev_project.data.TtsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.filter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/**
 * Sets up the immersive mode and handles displaying the entire screen
 * Get data from table of contents, fetch content from chapters from view model.
 * general sources: - Week 12: Room Database slide 63
 *                  - https://developer.android.com/develop/ui/views/layout/immersive
 *                  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/map-not-null.html
 *                  - https://developer.android.com/develop/ui/compose/side-effects
 */
@Composable
fun ReadingScreen (bookId: Int,
                   chapterId: Int,
                   onSearch: () -> Unit,
                   onBack: () -> Unit,
                   initialScrollRatio: Float = -1f,
                   searchQuery: String = "",
                   onToggleNavBar: (Boolean) -> Unit = {},
                   viewModel: RetrieveDataViewModel = hiltViewModel(),
                   //ttsVM: TTsViewModel = hiltViewModel()
    ){
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
//        if (selectedIndex >= 0 && chapters.isNotEmpty()) {
//            chapters[selectedIndex].chapterId?.let { ttsVM.prepareChapterById(it) }
//        }
    }
//    DisposableEffect(Unit) {
//        onDispose { ttsVM.releaseTTs() }
//    }
    if (chapters.isEmpty() || contents.isEmpty()) {
        LoadingIndicator()
    } else {
        Box(modifier = Modifier.clickable { toggleImmersiveMode() }) {
            ReadingPageContent(
                chapters = chapters,
                contents = contents,
                chapterIndexSelected = selectedIndex,
                onSearch = onSearch,
                onBack = onBack,
                //ttsVM = ttsVM
                initialScrollRatio = initialScrollRatio,
                searchQuery = searchQuery
            )
            if (isImmersive) {
                Text(
                    text = stringResource(R.string.tap_anywhere_to_exit_fullscreen),
                    modifier = Modifier.padding(8.dp).testTag("fullscreen_text")
                )
            }else{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    TTSControlBar()
                }
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
    modifier : Modifier = Modifier,
    //ttsVM: TTsViewModel
    initialScrollRatio: Float = -1f,
    searchQuery: String,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(chapterIndexSelected) {
        listState.scrollToItem(chapterIndexSelected)
    }
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(chapters) { index, chapter ->
            val contentText = contents.find { it.chapterId == chapter.chapterId }?.content ?: ""

            // only selected chapter use the ratio
            val chapterScrollRatio =
                if (index == chapterIndexSelected) {
                    initialScrollRatio
                } else {
                    -1f
                }

            chapter.contentId?.let {
                ChapterPage(
                    title = chapter.chapterTitle.toString(),
                    content = contentText,
                    contentId = it,
                    onSearch = onSearch,
                    onBack = onBack,
                    //ttsVM = ttsVM
                    searchQuery = searchQuery,
                    initialScrollRatio =
                        if (index == chapterIndexSelected) {
                            initialScrollRatio
                        } else {
                            -1f
                        }
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
    //ttsVM: TTsViewModel,
    viewModel: PositionViewModel = hiltViewModel(),
    initialScrollRatio: Float = -1f,
    searchQuery: String = ""
) {
    val state = rememberScrollState()
    // restore last saved scroll position
    LaunchedEffect(contentId) {
        if (initialScrollRatio < 0f) {
            viewModel.getScrollPosition(contentId)?.let { saved ->
                state.scrollTo(saved.toInt())
            }
        }
    }
    //go to ratio only after maxValue is ready
    LaunchedEffect(contentId, initialScrollRatio) {
        if (initialScrollRatio >= 0f) {
            // wait until layout has measured the content & maxValue > 0
            val max = snapshotFlow { state.maxValue }
                .filter { it > 0 }
                .first()

            val target = (max * initialScrollRatio).toInt()
            state.scrollTo(target)
            viewModel.saveScrollPosition(contentId, target.toFloat())
        }
    }
    LaunchedEffect(contentId, state.value) {
        viewModel.saveScrollPosition(contentId, state.value.toFloat())
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.padding_reg), vertical = dimensionResource(R.dimen.space_xxl)).verticalScroll(state)
        ) {
            SearchButton(onSearch)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_reg)))
            ChapterTitle(title)
            ChapterContent(content = content, highlightQuery = searchQuery)
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
fun ChapterContent(content: String, highlightQuery: String, modifier: Modifier = Modifier) {
    // build highlighted text every recomposition
    val annotated: AnnotatedString =
        if (highlightQuery.isBlank()) {
            AnnotatedString(content)
        } else {
            val lower = content.lowercase()
            val q = highlightQuery.lowercase()
            var start = 0
            buildAnnotatedString {
                while (true) {
                    val index = lower.indexOf(q, startIndex = start)
                    if (index < 0) {
                        append(content.substring(start))
                        break
                    }
                    //text befo the match
                    append(content.substring(start, index))
                    //highlighted match
                    withStyle(
                        SpanStyle(
                            background = MaterialTheme.colorScheme.secondaryContainer,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(content.substring(index, index + q.length))
                    }
                    start = index + q.length
                }
            }
        }
    Column(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp -
    dimensionResource(R.dimen.space_lg))) {
        Text(text = annotated,
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
fun TTSControlBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top=12.dp), horizontalArrangement = Arrangement.Center
    ){
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 4.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ) {
            //val state = viewModel.ttsState.collectAsState()
            Row(
                modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {}, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = CircleShape
                ) { // when we get viewmodel, i edit this: viewModel.stopTTs()
                    Icon(Icons.Default.Stop, contentDescription = stringResource(R.string.stop))
                }
                var isTTSEnabled = true //state.value.isPlaying
                Button(
                    onClick = {
                        if (isTTSEnabled) {
                            isTTSEnabled = false //.pauseTTs()
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = CircleShape
                ) {
                    val icon = if (isTTSEnabled) Icons.Default.Pause else Icons.Default.PlayArrow
                    Icon(
                        icon,
                        contentDescription = if (isTTSEnabled) stringResource(R.string.pause) else stringResource(
                            R.string.play
                        )
                    )
                }
            }
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
    onBack: () -> Unit = {},
    //ttsVM : TTsViewModel = hiltViewModel()
) {
    ReadingPageContent(
        chapters = chapters,
        contents = contents,
        chapterIndexSelected = chapterIndexSelected,
        onSearch = onSearch,
        onBack = onBack,
        //ttsVM = ttsVM
        initialScrollRatio = -1f,
        searchQuery = ""
    )
}




