package com.example.mobile_dev_project.data.repository

import android.util.Log
import java.io.File
import javax.inject.Inject
import com.example.mobile_dev_project.data.dao.*
import com.example.mobile_dev_project.data.entity.*
import com.example.mobile_dev_project.data.BooksPaths
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParsingRepository @Inject constructor(
    private val paths: BooksPaths,
    private val bookd: BookDao,
    private val chapterd: ChapterDao,
    private val contentd: ContentDao
){
    suspend fun parseHtml(bookId: Int) = withContext(Dispatchers.IO){
        try {
            //get html file
            val directory = paths.bookContentFolder(bookId.toString())
            //val htmlFile = contentFolder.listFiles()
            //    ?.firstOrNull { it.name.endsWith(".html", ignoreCase = true) }

            //this is better bcs on other websites, maybe html file is under nested subfolder
            //src: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/walk-top-down.html
            val html = directory.walkTopDown().firstOrNull {it.extension.equals("html", ignoreCase = true)}

            if (html == null) {
                Log.e("ParsingRepository", "HTML file not foudn for book: $bookId.")
                return@withContext
            }

            val htmlContent = html.readText(Charsets.UTF_8)

            val doc: Document = Ksoup.parse(htmlContent)

            //get the title that is inside meta tag and if doesnt exist, just get title from title tag
            val title = doc.selectFirst("meta[name=dc.title]")?.attr("content")?.trim()
                ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
                ?: doc.selectFirst("h1")?.text()?.trim()
                ?: "Untitled Book"

            //get chapters & their content
            val chapters = doc.select("div.chapter")
            val order = 1
            for( c in chapters){
                val chapTitle = c.select("h2").text().trim()
                val content = c.select("p")

                val formattedContent = content.joinToString( "\n" ) {it.outerHtml()}
            }

        } catch (e: Exception){
            Log.e("ParsingRepository", "Error occurred while parsing book with id $bookId: ${e.message}")
        }

    }

}