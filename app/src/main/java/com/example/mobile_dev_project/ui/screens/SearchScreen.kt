package com.example.mobile_dev_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.mobile_dev_project.R

/**
 * TextField to search within the current book
 * hows count and vertically scrollable results list
 * Highlights query in results
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }

    // Example content so UI works now
    val paragraphs = remember {
        listOf(
            "Chapter 1 : THE BOY WHO LIVED",
            "Chapter 2 : THE VANISHING GLASS",
            "Chapter 3 : THE LETTERS FROM NO ONE",
            "Chapter 4 : THE KEEPER OF THE KEYS"
        )
    }

    val pad = dimensionResource(id = R.dimen.space_md)

    // here, only compute raw matches in remember
    //  so return (index, paragraph) and build the highlighted text later inside composables
    val matches = remember(query) {
        if (query.isBlank()) {
            emptyList()
        }
        else paragraphs.mapIndexedNotNull { idx, p ->
            if (p.contains(query, ignoreCase = true)) {
                idx to p
            } else {
                null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.search_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                }
            )
        }
    ) {

    }
}

@Composable
private fun highlight(text: String, query: String): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)
    val lower = text.lowercase()
    val q = query.lowercase()
    var start = 0

    return buildAnnotatedString {
        while (true) {
            val i = lower.indexOf(q, start)
            if (i < 0) {
                append(text.substring(start))
                break
            }
            append(text.substring(start, i))
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                append(text.substring(i, i + q.length))
            }
            start = i + q.length
        }
    }
}