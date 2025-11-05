package com.example.mobile_dev_project.data.repository
import com.example.mobile_dev_project.data.BooksPaths
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.util.UnzipUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val paths: BooksPaths,
    private val downloader: OkHttpDownloader,
    private val unzipper: UnzipUtils,
    private val parser: ParsingRepository,
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val contentDao: ContentDao
) {
    ///Room passthroughs
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    fun getBookById(bookId: Int): Flow<Book?> {
        return bookDao.getBookById(bookId)
    }

    fun getBooksByLastAccessed(): Flow<List<Book>> {
        return bookDao.getBooksByLastAccessed()
    }

    fun getBooksByDateAdded(): Flow<List<Book>> {
        return bookDao.getBooksByDateAdded()
    }

    suspend fun insertBook(book: Book): Long {
        return bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }
}

