package com.example.mobile_dev_project.data.repository

import android.util.Log
import java.io.File
import javax.inject.Inject
import com.example.mobile_dev_project.data.*
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.BooksPaths
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParsingRepository @Inject constructor(
    private val paths: BooksPaths,
){
    //Locate the html file from the created book folder upon url entry and extract all metadata needed for storing later.
    //for the basics: https://github.com/fleeksoft/ksoup
    suspend fun parseHtml(bookId: Long): Pair<UiBook, List<UiContent>> = withContext(Dispatchers.IO){
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
                return@withContext Pair(
                    UiBook(bookId = bookId.toInt(), title = "Untitled Book", coverPath = "", chapters = emptyList()),
                    emptyList()
                )
            }
            val htmlContent = html.readText(Charsets.UTF_8)
            val doc: Document = Ksoup.parse(htmlContent)

            //get the title that is inside meta tag and if doesnt exist, just get title from title tag
            val bookTitle = extractBookTitle(doc)

            //get chapters & their content
            val (chapters, contents) = extractChapterAndContent(bookId.toInt(), directory, doc)

            //create uibook using title
            val uiBook = UiBook(bookId.toInt(), chapters, bookTitle,"")
            Pair(uiBook, contents)
        } catch (e: Exception){
            Log.e("ParsingRepository", "Error occurred while parsing book with id $bookId: ${e.message}")
            Pair(
                UiBook(bookId = bookId.toInt(), title = "Untitled Book", coverPath = "", chapters = emptyList()),
                emptyList()
            )
        }

    }

    /**
     * Locate the title that is inside <meta> tag with name="dc.title".
     * Extract the title.
     */
    private fun extractBookTitle(doc: Document): String{
        val title = doc.selectFirst("meta[name=dc.title]")?.attr("content")?.trim()
            ?: doc.selectFirst("h1")?.text()?.trim()
            ?: "Untitled Book"

        return title
    }

    /**
     * Locate <div> with the class name "chapter".
     * Within that div, each one has a <h2> that represents their title,
     * and each one has <p> tags that represents all their content.
     * Extract the books chapters and their corresponding content.
     */
    private fun extractChapterAndContent(bookId: Int, directory: File, doc: Document)
            :Pair<List<UiChapter>, List<UiContent>>{
        val chapters = mutableListOf<UiChapter>()
        val contents = mutableListOf<UiContent>()

        val chapterDivs = doc.select("div.chapter")
        //might need to do smt here so that it doesnt save "toc" chapter
        var order = 1
        for( c in chapterDivs){
            val chapTitle = c.select("h2").text().trim()
            val contentp = c.select("p")

            val formattedContent = contentp.joinToString( "\n" ) {it.outerHtml()}
            saveChaptersInHtml(directory, order, chapTitle, formattedContent)

            val uiChapter = UiChapter(null, chapTitle, order, bookId, null)
            chapters.add(uiChapter)


            val uiContent = UiContent(null, 0, formattedContent) // temporarily chapter id is set to 0
            contents.add(uiContent)

            order++
        }
        return Pair(chapters, contents)
    }
    /**
     * Save processed chapters as seperate HTML files
     */
    private fun saveChaptersInHtml(directory: File, order: Int, title: String, content: String){
        //IT SHOULD EXIST. since we retrieved it earlier.
//        if(!directory.exists()){
//            directory.mkdirs()
//        }

        // no weird spacing or characters for folder names
        val formattedTitle = title.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        val htmlName = "${order}_$formattedTitle.html"
        val file = File(directory, htmlName)

        file.writeText(content, Charsets.UTF_8)
    }

}