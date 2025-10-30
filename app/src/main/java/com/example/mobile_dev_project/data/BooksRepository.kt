package com.example.mobile_dev_project.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

/**
 * this is the data layer entry point for books
 * so ui never touches files or network, and ViewModel calls only this class
 * I refer week10 & week 11 lecture slide & gitlab code & lab
 * after download, unzip to per book content folder
 * refer week 9logging error, week10, week11
 */
class BooksRepository(
    private val paths: BooksPaths,
    private val downloader: OkHttpDownloader = OkHttpDownloader()
) {
    data class Book(
        val id: String,
        val title: String,
        // path to .../content
        val rootDir: String,
        //path to cover image
        val coverPath: String?
    )

    /**
     * make sure each (id,url) is present locally & ready to read
     * -download the zip if missing
     * - unzip into /content if empty
     * -find cover & return models for ui
     */
    suspend fun prepareBooks(sources: List<Pair<String, String>>): List<Book> =
        // refer week 10 suspend
        withContext(Dispatchers.IO) {
            for ((id, url) in sources) {
                val zip = paths.bookZipFolder(id)
                val content = paths.bookContentFolder(id)

                if (!zip.exists()) {
                    try {
                        downloader.downloadTo(url, zip)
                    } catch (e: Exception) {
                        Log.e("BooksRepository", "Download failed: $url : ${e.message}")
                        throw e
                    }
                }

                if (content.listFiles().isNullOrEmpty()) {
                    //Clean and unzip
                    if (content.exists()) content.deleteRecursively()
                    content.mkdirs()
                    try {
                        UnzipUtils.unzip(zip, content)
                    } catch (e: Exception) {
                        Log.e("BooksRepository", "unzip failed for ${zip.absolutePath}: ${e.message}")
                        throw e
                    }
                }
            }

            //building models for ui
            sources.map { (id, _) ->
                val root = paths.bookContentFolder(id)
                //II will write titleFor and findCover functions under
                Book(id = id, title = titleFor(id), rootDir = root.absolutePath, coverPath = findCover(root)?.absolutePath)
            }
        }

}
