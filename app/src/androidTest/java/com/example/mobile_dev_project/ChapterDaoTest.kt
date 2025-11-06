package com.example.mobile_dev_project

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.database.BookRoomDatabase
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.entity.Chapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ChapterDaoTest {

    private lateinit var chapterDao: ChapterDao
    private lateinit var bookDao: BookDao
    private lateinit var bookRoomDatabase: BookRoomDatabase
    private var testBookId: Int = 0

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        bookRoomDatabase = Room.inMemoryDatabaseBuilder(context, BookRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        chapterDao = bookRoomDatabase.chapterDao()
        bookDao = bookRoomDatabase.bookDao()

        // Create test book for chapters
        runBlocking {
            val book = Book()
            book.bookTitle = "Book"
            book.sourceUrl = "https://stuff.com"
            book.bookAdded = "2024-01-01"
            testBookId = bookDao.insertBook(book).toInt()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookRoomDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertChapter_insertsChapterIntoDatabase() = runBlocking {
        val chapter = Chapter()
        chapter.bookId = testBookId
        chapter.chapterName = "Morty"
        chapter.chapterOrder = 1

        val chapterId = chapterDao.insertChapter(chapter)

        Assert.assertTrue(chapterId > 0)
    }

    @Test
    @Throws(Exception::class)
    fun getAllChapters_returnsAllInsertedChapters() = runBlocking {
        val chapter1 = Chapter()
        chapter1.bookId = testBookId
        chapter1.chapterName = "Morty"
        chapter1.chapterOrder = 1

        val chapter2 = Chapter()
        chapter2.bookId = testBookId
        chapter2.chapterName = "Morty2"
        chapter2.chapterOrder = 2

        chapterDao.insertChapter(chapter1)
        chapterDao.insertChapter(chapter2)

        val chapters = chapterDao.getAllChapters().first()

        Assert.assertEquals(2, chapters.size)
        Assert.assertEquals("Morty", chapters[0].chapterName)
        Assert.assertEquals("Morty2", chapters[1].chapterName)
    }

    @Test
    @Throws(Exception::class)
    fun getChapterById_returnsCorrectChapter() = runBlocking {
        val chapter = Chapter()
        chapter.bookId = testBookId
        chapter.chapterName = "Morty"
        chapter.chapterOrder = 1

        val chapterId = chapterDao.insertChapter(chapter)
        val retrievedChapter = chapterDao.getChapterById(chapterId.toInt()).first()

        Assert.assertNotNull(retrievedChapter)
        Assert.assertEquals("Morty", retrievedChapter?.chapterName)
    }

    @Test
    @Throws(Exception::class)
    fun getChapterByOrder_returnsCorrectChapter() = runBlocking {
        val chapter = Chapter()
        chapter.bookId = testBookId
        chapter.chapterName = "Morty2"
        chapter.chapterOrder = 2

        chapterDao.insertChapter(chapter)
        val retrievedChapter = chapterDao.getChapterByOrder(testBookId, 2).first()

        Assert.assertNotNull(retrievedChapter)
        Assert.assertEquals("Morty2", retrievedChapter?.chapterName)
    }


}