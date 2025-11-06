package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeContentRepository(contentDao: ContentDao) : ContentRepository(contentDao) {
    //https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/mutable-map-of.html
    private val positions = mutableMapOf<Int, Float>()
    private val contents = mutableListOf<Content>()

    override fun getContentForChapter(chapterId: Int): Flow<Content?> {
        val content = contents.find { it.chapterId == chapterId }
        return flowOf(content)
    }

    override suspend fun insertContent(content: Content) {
        contents.add(content)
    }

    override suspend fun getScreenPosition(contentId: Int): Float? {
        return positions[contentId]
    }

    override suspend fun updateScreenPosition(contentId: Int, scrollPosition: Float) {
        positions[contentId] = scrollPosition
    }
}
