package com.example.mobile_dev_project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_dev_project.data.entity.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Insert
    suspend fun insertChapter(chapter: Chapter)

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Delete
    suspend fun deleteChapter(chapter: Chapter)


    @Query("select * from chapters")
    fun getAllChapters(): Flow<List<Chapter>>

    @Query("select * from chapters where chapterId = :chapterId")
    fun getChapterById(chapterId: Int): Flow<Chapter?>

    @Query("select * from chapters where bookId = :bookId order by chapterOrder asc")
    fun getChaptersForBook(bookId: Int): Flow<List<Chapter>>

    @Query("select * from chapters where bookId = :bookId and chapterOrder = :order")
    fun getChapterByOrder(bookId: Int, order: Int): Flow<Chapter?>

    @Query("delete from chapters where bookId = :bookId")
    suspend fun deleteChaptersForBook(bookId: Int)
}