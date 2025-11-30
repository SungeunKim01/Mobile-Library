package com.example.mobile_dev_project.data.mappers

import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.entity.Content as ContentEntity

fun UiContent.toEntity(): ContentEntity {
    val entity = ContentEntity(
        chapterId = this.chapterId,
        content = this.content
    )
    entity.contentId = this.contentId ?: 0
    return entity
}

fun ContentEntity.toUi(): UiContent {
    return UiContent(
        contentId = this.contentId,
        chapterId = this.chapterId,
        content = this.content ?: ""
    )
}
