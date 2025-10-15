package com.example.mobile_dev_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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