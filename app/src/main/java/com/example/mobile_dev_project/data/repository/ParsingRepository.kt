package com.example.mobile_dev_project.data.repository

import android.util.Log
import java.io.File
import javax.inject.Inject
import com.example.mobile_dev_project.data.dao.*
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
    private val bookd: BookDao,
    private val chapterd: ChapterDao,
    private val contentd: ContentDao
){
    //Locate the html file from the created book folder upon url entry and store metadata in database.
    //for the basics: https://github.com/fleeksoft/ksoup
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
            updateBookTitle(bookId, doc)

            //get chapters & their content
            storeChapterAndContentData(bookId, directory, doc)

        } catch (e: Exception){
            Log.e("ParsingRepository", "Error occurred while parsing book with id $bookId: ${e.message}")
        }

    }

    /**
     * Locate the title that is inside <meta> tag with name="dc.title".
     * Store the title in provided bookId.
     */
    private suspend fun updateBookTitle(bookId: Int, doc: Document){
        val title = doc.selectFirst("meta[name=dc.title]")?.attr("content")?.trim()
            ?: doc.selectFirst("h1")?.text()?.trim()
            ?: "Untitled Book"

        val bookToUpdate = bookd.getSingularBookById(bookId)
        if(bookToUpdate != null) {
            bookToUpdate.bookTitle = title
            bookd.updateBook(bookToUpdate)
        }
    }

    /**
     * Locate <div> with the class name "chapter".
     * Within that div, each one has a <h2> that represents their title,
     * and each one has <p> tags that represents all their content.
     * Store the books chapters and their corresponding content.
     */
    private suspend fun storeChapterAndContentData(bookId: Int, directory: File, doc: Document){
        val chapters = doc.select("div.chapter")
        //might need to do smt here so that it doesnt save "toc" chapter
        var order = 1
        for( c in chapters){
            val chapTitle = c.select("h2").text().trim()
            val contentp = c.select("p")

            val formattedContent = contentp.joinToString( "\n" ) {it.outerHtml()}
            saveChaptersInHtml(directory, order, chapTitle, formattedContent)

            val chapter = Chapter(bookId, chapTitle, order, 0)
            val chapId = chapterd.insertChapter(chapter)
            val content = Content(chapId.toInt(), formattedContent)
            val contentId = contentd.insertContent(content)

            //now that we have content id, add it to the created chapter
            chapter.contentId = contentId.toInt()
            chapterd.updateChapter(chapter)

            order++
        }
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