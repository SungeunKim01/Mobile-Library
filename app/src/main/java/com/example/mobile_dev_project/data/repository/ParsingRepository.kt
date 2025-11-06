package com.example.mobile_dev_project.data.repository

import android.util.Log
import java.io.File
import javax.inject.Inject
import com.example.mobile_dev_project.data.*
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
    suspend fun parseHtml(bookId: String): Pair<UiBook, List<UiContent>> = withContext(Dispatchers.IO){
        try {
            //get html file
            val directory = paths.bookContentFolder(bookId)
            //this is better bcs on other websites, maybe html file is under nested subfolder
            //src: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/walk-top-down.html
            val html = directory
                .walkTopDown().firstOrNull { f ->
                    val extension = f.extension.lowercase()
                    extension == "html" || extension == "htm"
                }
            if (html == null) {
                Log.e("ParsingRepository", "HTML file not foudn for book: $bookId.")
                return@withContext Pair(
                    UiBook(bookId = null, title = "Untitled Book", coverPath = "", chapters = emptyList()),
                    emptyList()
                )
            }
            val htmlContent = html.readText(Charsets.UTF_8)
            val doc: Document = Ksoup.parse(htmlContent)
            //get the title that is inside meta tag and if doesnt exist, just get title from title tag
            val bookTitle = extractBookTitle(doc)
            val image = findCoverImage(bookId, paths)
            //get chapters & their content
            val (chapters, contents) = extractChapterAndContent(-1, directory, doc)

            //create uibook using title
            val uiBook = UiBook(bookId = null, chapters = chapters, title = bookTitle, coverPath = "")
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
     * chapter and content extraction, returns Pair<List<UiChapter>, List<UiContent>>
     * 1) Container based - if the source uses <div class="chapter"> per chapter:
     * -Pick a display title from first h2/h3 in that div or default Chapter n
     * - drop boilerplate within each chapter block
     * -sve the chapter's html to file for offline viewing and caching
     *
     * 2 Heading based -If there are no div.chapter wrappers:
     * - collect h2/h3 headings that look like chapter headings - CHAPTER n/roman num/digit
     * - walk the doc forward (not only siblings) to collect the content until the next heading
     * - clean boilerplate & save to file, then emit UiChapter and UiContent
     *
     * 3)fallback -if neither pattern matches, put entire <body> as one chapter
     */
    private fun extractChapterAndContent(bookId: Int, directory: File, doc: Document)
            :Pair<List<UiChapter>, List<UiContent>> {
        val pairs = mutableListOf<Pair<UiChapter, UiContent>>()

        // div.chapter layout
        val chapterDivs = doc.select("div.chapter")
        if (chapterDivs.isNotEmpty()) {
            var order = 1
            for (c in chapterDivs) {
                // explicit heading within the chapter block (h2/h3) & Clean whitespace
                val raw = c.select("h2, h3").firstOrNull()?.text()?.trim()
                val chapTitle =
                    if (raw.isNullOrBlank()) {
                        "Chapter $order"
                    } else {
                        raw
                    }

                // strip boilerplate inside each chapter
                c.select("div#titlepage, div.agate, div.advertisement").remove()

                //chapter payload becomes the remaining html inside this chapter div
                val chapterHtml = c.html()
                // persist each chapter chunk as an individual html file
                saveChaptersInHtml(directory, order, chapTitle, chapterHtml)

                val uiChapter = UiChapter(null, chapTitle, order, bookId, null)
                val uiContent = UiContent(null, 0, chapterHtml)
                pairs += (uiChapter to uiContent)
                order++
            }
            return Pair(pairs.map{it.first}, pairs.map{it.second})
        }

        // heading based - no div.chapter
        // filter h2/h3 that resemble real chapter headings
        val headings = doc.select("h2, h3").filter { looksLikeChapter(it.text()) }
        if (headings.isNotEmpty()) {
            var order = 1

            //from Dorian's referring source - https://github.com/fleeksoft/ksoup
            // class i used - com.fleeksoft.ksoup.nodes.Element
            fun isHeading(e: com.fleeksoft.ksoup.nodes.Element?): Boolean {
                if (e == null) return false
                val tag = e.tagName().lowercase()
                return tag == "h2" || tag == "h3"
            }

            // walk forward in document order, not just direct siblings
            fun nextBlock(cur: com.fleeksoft.ksoup.nodes.Element?): com.fleeksoft.ksoup.nodes.Element? {
                // try nxt element sibling
                cur?.nextElementSibling()?.let { return it }
                // otherwise, climb up until can go right, then down
                var up = cur?.parent()
                var child: com.fleeksoft.ksoup.nodes.Element? = cur
                while (up != null) {
                    val next = child?.nextElementSibling()
                    if (next != null) {
                        return next
                    }
                    child = up
                    up = up.parent()
                }
                return null
            }
            // for each chapter like heading, gather all subsequent blocks until the next heading
            for ((i, h) in headings.withIndex()) {
                val raw = h.text().trim()
                val chapTitle =
                    if (raw.isBlank()) {
                        "Chapter $order"
                    } else {
                        raw
                    }

                val nextHeading = headings.getOrNull(i + 1)

                // accumulate everything after this heading until the nxt heading
                val chunk = StringBuilder()
                var node = nextBlock(h)
                while (node != null && node != nextHeading && !isHeading(node)) {
                    // strip boilerplate that I do noy want to store
                    node.select("div#titlepage, div.agate, div.advertisement").remove()
                    chunk.append(node.outerHtml()).append('\n')
                    node = nextBlock(node)
                }
                // if nothing accumulated, fallback to the nxt immediate block or empty paragraph
                val chapterHtml = if (chunk.isEmpty()) h.nextElementSibling()?.outerHtml() ?: "<p></p>" else chunk.toString()
                saveChaptersInHtml(directory, order, chapTitle, chapterHtml)

                val uiChapter = UiChapter(null, chapTitle, order, bookId, null)
                val uiContent = UiContent(null, 0, chapterHtml)
                pairs += (uiChapter to uiContent)
                order++
            }
            return Pair(pairs.map{it.first}, pairs.map{it.second})
        }

        // nothing matched
        val chapters = UiChapter(null, "Untitled", 1, bookId, null)
        val contents = UiContent(null, 0, doc.body()?.html().orEmpty())
        pairs += (chapters to contents)
        return Pair(pairs.map{it.first}, pairs.map{it.second})
    }

    //helper -save processed chapters as seperate html files
    // referred this website for regex method: https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/to-regex.html
    private fun saveChaptersInHtml(directory: File, order: Int, title: String, content: String){
        // no weird spacing OR chars for folder names
        // here im sing regex
        val formattedTitle = title.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        val htmlName = "${order}_$formattedTitle.html"
        val file = File(directory, htmlName)

        file.writeText(content, Charsets.UTF_8)
    }

    //helper - heading looks like a chapter - accet roman num or digits after CHAPTER
    //Also I refer this website for using Regex: https://www.geeksforgeeks.org/kotlin/kotlin-regular-expression/
    private fun looksLikeChapter(text: String): Boolean {
        val t = text.trim().uppercase()

        if (t.contains("CONTENTS") || t.contains("ILLUSTRATIONS")) return false

        // common gutenberg patterns
        return t.matches(Regex("""^(CHAPTER|BOOK|SECTION|STAVE)\b.*""")) ||
                t.matches(Regex("""^([IVXLCDM]+|[0-9]+)[\.\s].*""")) ||
                t.startsWith("CHAPTER ") ||
                t.startsWith("STAVE ") ||
                t.startsWith("BOOK ") ||
                t.startsWith("SECTION ")
    }

    /**
     * Find cover image in images folder and return it's absolute path to store in db later.
     */
    private fun findCoverImage(bookId: String, paths: BooksPaths): String? {
        val imagesFolder = paths.bookImagesFolder(bookId)
        val possibleCovers = listOf("cover.jpg", "cover.jpeg", "cover.png")

        val coverFile = imagesFolder
            .listFiles()
            ?.firstOrNull { file ->
                possibleCovers.any { name -> file.name.equals(name, ignoreCase = true) }
            }

        //store the absolute path in db
        return coverFile?.absolutePath
    }

}
