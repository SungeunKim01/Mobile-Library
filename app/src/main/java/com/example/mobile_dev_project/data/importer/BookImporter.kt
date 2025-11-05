package com.example.mobile_dev_project.data.importer

import com.example.mobile_dev_project.data.BooksPaths
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.repository.OkHttpDownloader
import com.example.mobile_dev_project.data.repository.ParsingRepository
import com.example.mobile_dev_project.data.util.UnzipUtils
import com.example.mobile_dev_project.ui.model.ProgressState
import com.example.mobile_dev_project.ui.model.ImportPhase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * 1-Download ZIP to /files/books/{id}/{id}.zip
 * 2 - unzip to /files/books/{id}/content
 * 3 Parse html(ParsingRepository) - UiBook and UiContent
 * 4 - Save to Room (Book, Chapter, Content)
 */
class BookImporter @Inject constructor(
    private val paths: BooksPaths,
    private val downloader: OkHttpDownloader,
    private val unzipper: UnzipUtils,
    private val parser: ParsingRepository,
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val contentDao: ContentDao,
) {
    //display progress msg
    suspend fun importBooks(
        sources: List<Pair<String, String>>,
        onProgress: (ProgressState) -> Unit
    ) = withContext(Dispatchers.IO) {
        //downloading
        onProgress(ProgressState(ImportPhase.DOWNLOADING, "Starting downloads..."))

        sources.forEachIndexed { index, (bookId, url) ->
            // /files/books/{id}/{id}.zip
            val zip: File = paths.bookZipFolder(bookId)
            // /files/books/{id}/content
            val contentDir: File = paths.bookContentFolder(bookId)

            // download if missing
            if (!zip.exists()) {
                downloader.downloadTo(url, zip) { note ->
                    onProgress(ProgressState(ImportPhase.DOWNLOADING, note, detail = url)) }
                onProgress(
                    ProgressState(
                        ImportPhase.DOWNLOADING,
                        "Downloaded ${index + 1}/${sources.size}",
                        detail = bookId
                    )
                )
            } else {
                onProgress(ProgressState(ImportPhase.DOWNLOADING, "Already downloaded", detail = bookId))
            }

            // unzip
            if (contentDir.listFiles().isNullOrEmpty()) {
                if (contentDir.exists()) contentDir.deleteRecursively()
                contentDir.mkdirs()

                onProgress(ProgressState(ImportPhase.UNZIPPING, "Unzipping $bookId…"))
                unzipper.unzip(zip, contentDir)
                onProgress(ProgressState(ImportPhase.UNZIPPING, "Unzip complete", detail = bookId))
            } else {
                onProgress(ProgressState(ImportPhase.UNZIPPING, "Already unzipped", detail = bookId))
            }

            // parsing
            onProgress(ProgressState(ImportPhase.PARSING, "Parsing html for $bookId…"))
            val (uiBook, uiContents) = parser.parseHtml(bookId)
            onProgress(ProgressState(ImportPhase.PARSING, "Parsed: ${uiBook.title}"))

            //populating(Room inserts)
            onProgress(ProgressState(ImportPhase.POPULATING, "Saving “${uiBook.title}” to database..."))

            //insert Book first
            val bookEntity = Book().apply {
                bookTitle = uiBook.title
                bookCoverPath = uiBook.coverPath
                val formatter = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault())
                val currentTime = formatter.format(java.util.Date())

                bookAdded = currentTime
                lastAccessed = currentTime
            }
            val newBookId = bookDao.insertBook(bookEntity).toInt()

            // insert Chapters and Content
            // pair content[i] with chapter[i]
            uiBook.chapters.forEachIndexed { i, uiCh ->
                val ch = Chapter().apply {
                    this.bookId = newBookId
                    chapterName = uiCh.chapterTitle
                    chapterOrder = uiCh.chapterOrder
                    // set after content insert
                    contentId = null
                }
                val newChapterId = chapterDao.insertChapter(ch).toInt()

                uiContents.getOrNull(i)?.let { uiC ->
                    val content = Content(chapterId = newChapterId, content = uiC.content)
                    val newContentId = contentDao.insertContent(content).toInt()

                    // if Chapter.contentId is Int?, assign Int
                    // if it's String?, store str
                    ch.chapterId = newChapterId
                    ch.contentId = newContentId
                    chapterDao.updateChapter(ch)
                }

                //per chapter msg - keep the user informed
                onProgress(
                    ProgressState(
                        ImportPhase.POPULATING,
                        "Inserted chapter ${i + 1} of ${uiBook.chapters.size}",
                        detail = uiCh.chapterTitle
                    )
                )
            }

            onProgress(ProgressState(ImportPhase.POPULATING, "Saved: ${uiBook.title}"))
        }

        //
        onProgress(ProgressState(ImportPhase.DONE, "All books ready"))
    }
}