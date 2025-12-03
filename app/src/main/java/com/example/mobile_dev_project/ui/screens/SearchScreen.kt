package com.example.mobile_dev_project.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.mobile_dev_project.R
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mobile_dev_project.data.SearchResult

/**
 * TextField to search within the current book
 * hows count and vertically scrollable results list
 * Highlights query in results
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    // this is callback when user taps a search result
    onNavigateToLocation: (SearchResult) -> Unit,
    vm: SearchViewModel = hiltViewModel(),
    onToggleNavBar: (Boolean) -> Unit = {}
) {
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

    val pad = dimensionResource(id = R.dimen.space_md)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.search_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("BackButton")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = modifier
                .padding(inner)
                .clickable { toggleImmersiveMode() }
                .padding(horizontal = pad, vertical = pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(pad)
        ) {
            //input composable
            SearchField(
                value = vm.query,
                onValueChange = vm::onQueryChanged
            )
            // result Count as separate composable
            ResultCount(
                count = vm.results.size,
                queryNotBlank = vm.query.isNotBlank()
            )
            //results List Composable
            SearchResultsList(
                matches = vm.results,
                query = vm.query,
                onResultClick = onNavigateToLocation
            )
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

//Text field for entering the search query str
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.medium,
        singleLine = true,
        label = { Text(stringResource(R.string.search_title)) },
        placeholder = { Text(stringResource(R.string.search_text)) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensionResource(R.dimen.field_height))
            .testTag("QueryField")
    )
}

//composable that show the # of results found or No matches
@Composable
fun ResultCount(
    count: Int,
    queryNotBlank: Boolean
) {
    val text = if (count == 0 && queryNotBlank)
        stringResource(R.string.no_results)
    else
        stringResource(R.string.results_fmt, count)

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.testTag("ResultsCount")
    )
}

//Display highlighted search results
@Composable
fun SearchResultsList(
    matches: List<SearchResult>,
    query: String,
    onResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_sm)),
        modifier = modifier.fillMaxSize()
    ) {
        // use contentId as key so Compose can reuse rows if list changes
        itemsIndexed( matches, key = { _, hit -> hit.contentId } ) {
            index, hit ->
            val annotated: AnnotatedString = highlight(hit.snippet, query)

            ElevatedCard(
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = dimensionResource(R.dimen.card_elevation) ),
                modifier = Modifier
                    .fillMaxWidth()
                    //navigate when tapped
                    .clickable { onResultClick(hit) }
                    .testTag("ResultCard_$index")
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.space_md))
                ) {
                    //show chapter title on top
                    Text( text = hit.chapterTitle, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(dimensionResource(R.dimen.space_xs)))
                    // show snippet w the query highlighted
                    Text( text = annotated, style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

//Highlights the query substring in the result
//If query is blank, returns AnnotatedString w no highlights
@Composable
fun highlight(text: String, query: String): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)
    val lower = text.lowercase()
    val q = query.lowercase()
    var start = 0
    val bg = MaterialTheme.colorScheme.onSurfaceVariant
    val pColour = MaterialTheme.colorScheme.onPrimary

    return buildAnnotatedString {
        while (true) {
            val i = lower.indexOf(q, start)
            if (i < 0) { 
                append(text.substring(start))
                break
            }
            append(text.substring(start, i))
            withStyle(
                SpanStyle(
                    background = bg,
                    fontWeight = FontWeight.Bold,
                    color = pColour
                )
            ) {
                append(text.substring(i, i + q.length))
            }
            start = i + q.length
        }
    }
}