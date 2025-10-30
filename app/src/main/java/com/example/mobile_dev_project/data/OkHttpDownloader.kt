package com.example.mobile_dev_project.data

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * download a url to a file using OkHttp
 * Call from a coroutine with Dispatchers.IO
 * I refer week 11 lecture slide and week 10 -Dispatchers
 */
class OkHttpDownloader(private val client: OkHttpClient = OkHttpClient()) {

    fun downloadTo(url: String, dest: File) {
        // avoid trailing period issue - happened before whe I try to download the url that professor sent us,
        //so I declare this cleanUrl variable
        val cleanUrl = url.trim().trimEnd('.', ',', ';')

        val request = Request.Builder().url(cleanUrl).build()

        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) {
                error("HTTP ${resp.code}")
            }
            val body = resp.body ?: error("Empty response body")
            dest.outputStream().use { out ->
                body.byteStream().copyTo(out)
            }
        }
    }
}
