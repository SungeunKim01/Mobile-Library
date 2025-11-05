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
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel

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
    vm: SearchViewModel = viewModel()
) {
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
                .padding(horizontal = pad, vertical = pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(pad)
        ) {
            OutlinedTextField(
                // read vm state
                value = vm.query,
                //event to vm
                onValueChange = vm::onQueryChanged,
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                label = { Text(stringResource(R.string.search_title)) },
                placeholder = { Text(stringResource(R.string.search_text)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.field_height))
                    // this is for ui test
                    .testTag("QueryField")
            )

            val countText = if (vm.matches.isEmpty() && vm.query.isNotBlank())
                stringResource(R.string.no_results)
            else
                stringResource(R.string.results_fmt, vm.matches.size)

            Text(
                text = countText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                // this is for ui test
                modifier = Modifier.testTag("ResultsCount")
            )

            // Build highlighted text inside composable items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_sm)),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(vm.matches, key = { idx, _ -> idx }) { idx, (_, paragraph) ->
                    val annotated: AnnotatedString = highlight(paragraph, vm.query)
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = dimensionResource(R.dimen.card_elevation)
                        ),
                        // this is for ui test
                        modifier = Modifier.testTag("ResultCard_$idx")
                    ) {
                        Column(Modifier.padding(pad)) {
                            Text(
                                text = "Match ${idx + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.space_xs)))
                            Text(text = annotated, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun highlight(text: String, query: String): AnnotatedString {
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

// function - no Compose/Android types, so for JVM unit tests
//Returns (position, paragraph) pairs where the paragraph contains [query], case insensitive
internal fun findMatches(
    paragraphs: List<String>,
    query: String
): List<Pair<Int, String>> {
    if (query.isBlank()) return emptyList()
    val q = query.lowercase()
    return paragraphs.mapIndexedNotNull { idx, p ->
        if (p.lowercase().contains(q)) idx to p else null
    }
}