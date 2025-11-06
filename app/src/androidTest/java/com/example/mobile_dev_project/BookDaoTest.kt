package com.example.mobile_dev_project

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.database.BookRoomDatabase
import com.example.mobile_dev_project.data.entity.Book
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var bookDao: BookDao
    private lateinit var bookRoomDatabase: BookRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        bookRoomDatabase = Room.inMemoryDatabaseBuilder(context, BookRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        bookDao = bookRoomDatabase.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookRoomDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertBook_insertsBookIntoDatabase() = runBlocking {
        val book = Book()
        book.bookTitle = "Book"
        book.sourceUrl = "https://Morty.com"
        book.bookAdded = "2024-01-01"

        val bookId = bookDao.insertBook(book)

        Assert.assertTrue(bookId > 0)
    }

    @Test
    @Throws(Exception::class)
    fun getAllBooks_returnsAllInsertedBooks() = runBlocking {
        val book1 = Book()
        book1.bookTitle = "Book 1"
        book1.sourceUrl = "https://Morty.com"
        book1.bookAdded = "2024-01-01"

        val book2 = Book()
        book2.bookTitle = "Book 2"
        book2.sourceUrl = "https://Rick.com"
        book2.bookAdded = "2024-01-02"

        bookDao.insertBook(book1)
        bookDao.insertBook(book2)

        val books = bookDao.getAllBooks().first()

        Assert.assertEquals(2, books.size)
        Assert.assertEquals("Book 1", books[0].bookTitle)
        Assert.assertEquals("Book 2", books[1].bookTitle)
    }

    @Test
    @Throws(Exception::class)
    fun getSingularBookById_returnsCorrectBook() = runBlocking {
        val book = Book()
        book.bookTitle = "Book 1"
        book.sourceUrl = "https://Morty.com"
        book.bookAdded = "2024-01-01"

        val bookId = bookDao.insertBook(book)
        val retrievedBook = bookDao.getSingularBookById(bookId.toInt())

        Assert.assertNotNull(retrievedBook)
        Assert.assertEquals("Book 1", retrievedBook?.bookTitle)
    }




    //This tests if the URL exists withing the db (Lots of issues with it)
    @Test
    @Throws(Exception::class)
    fun existsByUrl_returnsTrueWhenUrlExists() = runBlocking {
        val book = Book()
        book.bookTitle = "Book 1"
        book.sourceUrl = "https://Morty.com"
        book.bookAdded = "2024-01-01"

        bookDao.insertBook(book)
        val exists = bookDao.existsByUrl("https://Morty.com")

        Assert.assertTrue(exists)
    }


    //This tests that if this is the most recent one opened or added it will return that book
    @Test
    @Throws(Exception::class)
    fun getBooksByDateAdded_returnsInCorrectOrder() = runBlocking {
        val book1 = Book()
        book1.bookTitle = "Book 1"
        book1.sourceUrl = "https://Morty.com"
        book1.bookAdded = "2024-01-01"

        val book2 = Book()
        book2.bookTitle = "Book 2"
        book2.sourceUrl = "https://Rick.com"
        book2.bookAdded = "2024-01-05"

        bookDao.insertBook(book1)
        bookDao.insertBook(book2)

        val books = bookDao.getBooksByDateAdded().first()

        Assert.assertEquals("Book 2", books[0].bookTitle)
    }

}