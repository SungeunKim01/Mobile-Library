package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import com.example.mobile_dev_project.data.repository.TtsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TTsViewModel @Inject constructor(
    private val ttsRepository: TtsRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
): ViewModel(){
    var chapter: Flow<Chapter?>? = null
    var content: Flow<Content?>? = null

    fun setChapter(chapterId: Int){
        var chapter: Flow<Chapter?> = chapterRepository.getChapterById(chapterId)
    }
    fun setContent(){
        if(chapter != null){
            var chapterId = chapter
            content = contentRepository.getContentForChapter(chapterId)
        }
    }

    fun prepareTTs(){
        if(chapter != null && content != null){
            ttsRepository.prepare(chapter.Id, content.content)
        }
    }

    fun playTTs(){
        ttsRepository.play()
    }

    fun pauseTTs(){
        ttsRepository.pause()
    }

    fun stopTTs(){
        ttsRepository.stop()
    }
    fun releaseTTs(){
        ttsRepository.release()
    }

}