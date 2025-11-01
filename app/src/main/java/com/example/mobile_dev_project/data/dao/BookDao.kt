package com.example.mobile_dev_project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_dev_project.data.entity.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    //for data stuff
    @Query("select * from Book where bookId = :bookId Limit 1")
    suspend fun getSingularBookById(bookId: Int): Book?

    //for the ui, we need flows
    @Query("select * from Book")
    fun getAllBooks(): Flow<List<Book>>

    @Query("select * from Book where bookId = :bookId")
    fun getBookById(bookId: Int): Flow<Book?>

    @Query("Select * from book order by lastAccessed desc")
    fun getBooksByLastAccessed(): Flow<List<Book>>

    @Query("select * from book order by bookAdded desc")
    fun getBooksByDateAdded(): Flow<List<Book>>
}