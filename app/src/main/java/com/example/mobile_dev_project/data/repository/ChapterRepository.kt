package com.example.mobile_dev_project.data.repository

import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.entity.Chapter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao
) {
    val allChapters: Flow<List<Chapter>> = chapterDao.getAllChapters()

    fun getChapterById(chapterId: Int): Flow<Chapter?> {
        return chapterDao.getChapterById(chapterId)
    }

    fun getChaptersForBook(bookId: Int): Flow<List<Chapter>> {
        return chapterDao.getChaptersForBook(bookId)
    }

    fun getChapterByOrder(bookId: Int, order: Int): Flow<Chapter?> {
        return chapterDao.getChapterByOrder(bookId, order)
    }

    suspend fun insertChapter(chapter: Chapter) {
        chapterDao.insertChapter(chapter)
    }

    suspend fun updateChapter(chapter: Chapter) {
        chapterDao.updateChapter(chapter)
    }

    suspend fun deleteChapter(chapter: Chapter) {
        chapterDao.deleteChapter(chapter)
    }

    suspend fun deleteChaptersForBook(bookId: Int) {
        chapterDao.deleteChaptersForBook(bookId)
    }
}