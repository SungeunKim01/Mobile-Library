package com.example.mobile_dev_project.data.repository

import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.entity.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
    private val bookDao: BookDao
) {
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

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }
}
