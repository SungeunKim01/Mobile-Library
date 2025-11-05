package com.example.mobile_dev_project.data.util

import androidx.core.net.toUri

/**
 *  dedupe by url, but normalize variations-
 * lowercase scheme and host
 * drop trailing slash
 * drop fragment
 *keep query if present
 */
fun normalizeUrl(raw: String): String {
    val uri = raw.trim().toUri()
    val scheme = (uri.scheme ?: "https").lowercase()
    val host = (uri.host ?: "").lowercase()
    val path = (uri.path ?: "").trimEnd('/')
    val query = uri.query?.let { "?$it" } ?: ""
    return "$scheme://$host$path$query"
}