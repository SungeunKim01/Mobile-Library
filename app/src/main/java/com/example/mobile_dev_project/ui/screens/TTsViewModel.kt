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
    private val TTsRepository: TtsRepository,
    private val ChapterRepository: ChapterRepository,
    private val ContentRepository: ContentRepository
): ViewModel(){
    var chapter: Flow<Chapter?>? = null
    var content: Flow<Content?>? = null

    fun setChapter(chapterId: Int){
        var chapter: Flow<Chapter?> = ChapterRepository.getChapterById(chapterId)
    }
    fun setContent(){
        if(chapter != null){
            var chapterId = chapter
            content = ContentRepository.getContentForChapter(chapterId)
        }
    }

    fun prepareTTs(){
        if(chapter != null && content != null){
            TTsRepository.prepare(chapter.Id, content.content)
        }
    }

    fun playTTs(){
        TTsRepository.play()
    }

    fun pauseTTs(){
        TTsRepository.pause()
    }

    fun stopTTs(){
        TTsRepository.stop()
    }
    fun releaseTTs(){
        TTsRepository.release()
    }
    
}