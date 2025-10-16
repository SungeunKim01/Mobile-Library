package com.example.mobile_dev_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import com.example.mobile_dev_project.R
import androidx.compose.ui.platform.testTag


/**
 * TextField to enter a book URL
 * Primary button "Add to Library"
 * Use dimensions and strings, so no hardcoded dp or ext
 * OutlinedTextField uses theme shapes, Surface tonalElevation is set at theme root
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadBookScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var url by remember { mutableStateOf("") }

    val pad = dimensionResource(id = R.dimen.space_md)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.download_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("BackButton")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cancel))
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
                value = url,
                onValueChange = { url = it },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                label = { Text(stringResource(R.string.download_title)) },
                placeholder = { Text(stringResource(R.string.download_url)) },
                visualTransformation = VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.field_height))
                    .testTag("UrlField")
            )

            Button(
                onClick = { /* later milestone */ },
                enabled = canEnableAdd(url),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("AddButton")
            ) {
                Text(text = stringResource(R.string.download_action))
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CancelButton")
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}

// unit testable rule that mirrors current behavior - non empty URL enables the button
internal fun canEnableAdd(url: String): Boolean = url.isNotBlank()
