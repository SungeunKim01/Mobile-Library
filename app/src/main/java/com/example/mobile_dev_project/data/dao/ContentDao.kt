package com.example.mobile_dev_project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_dev_project.data.entity.Content
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

    @Insert
    suspend fun insertContent(content: Content): Long

    @Update
    suspend fun updateContent(content: Content)

    @Delete
    suspend fun deleteContent(content: Content)


    @Query("select * from contents")
    fun getAllContents(): Flow<List<Content>>

    @Query("select * from contents where contentId = :contentId")
    fun getContentById(contentId: Int): Flow<Content?>

    @Query("Select * from contents where chapterId = :chapterId")
    fun getContentForChapter(chapterId: Int): Flow<Content?>

    @Query("Delete from contents where chapterId = :chapterId")
    suspend fun deleteContentForChapter(chapterId: Int)

    @Query("select contentLocation from contents where contentId = :contentId")
    suspend fun getScrollPosition(contentId: Int): Float?

    @Query("update contents set contentLocation = :scrollPosition where contentId = :contentId")
    suspend fun updateScrollPosition(contentId: Int, scrollPosition: Float)

}