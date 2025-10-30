package com.example.mobile_dev_project.data.repository

import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Content
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val contentDao: ContentDao
) {
    val allContents: Flow<List<Content>> = contentDao.getAllContents()

    fun getContentById(contentId: Int): Flow<Content?> {
        return contentDao.getContentById(contentId)
    }

    fun getContentForChapter(chapterId: Int): Flow<Content?> {
        return contentDao.getContentForChapter(chapterId)
    }

    suspend fun insertContent(content: Content) {
        contentDao.insertContent(content)
    }

    suspend fun updateContent(content: Content) {
        contentDao.updateContent(content)
    }

    suspend fun deleteContent(content: Content) {
        contentDao.deleteContent(content)
    }

    suspend fun deleteContentForChapter(chapterId: Int) {
        contentDao.deleteContentForChapter(chapterId)
    }
}
