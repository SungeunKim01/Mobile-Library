package com.example.mobile_dev_project.data

import android.content.Context
import java.io.File

/**
 * All file/folder paths for books
 * each book gets its own folder under /files/books/{bookId}
 * bookId}.zip - downloaded zip
 * /content/- unzipped html and images
 * I refer week11 lcture slide - internal storage
 */
class BooksPaths(private val appContext: Context) {
    private val booksRoot: File by lazy {
        File(appContext.filesDir, "books").apply { mkdirs() }
    }
    //per book folder- /files/books/{bookId}
    fun bookRoot(bookId: String): File =
        File(booksRoot, bookId).apply { mkdirs() }
    // downloaded zip path - /files/books/{bookId}/{bookId}.zip
    fun bookZipFile(bookId: String): File =
        File(bookRoot(bookId), "$bookId.zip")
    //unzipped html content - /files/books/{bookId}/content
    fun bookContentFolder(bookId: String): File =
        File(bookRoot(bookId), "content").apply { mkdirs() }

    // folder containing images: /files/books/{bookId}/content/images
    fun bookImagesFolder(bookId: String): File =
        File(bookContentFolder(bookId), "images").apply { mkdirs() }
}