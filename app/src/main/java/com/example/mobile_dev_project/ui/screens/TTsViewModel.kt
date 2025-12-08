package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.UiChapter
import com.example.mobile_dev_project.data.UiContent
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import com.example.mobile_dev_project.data.repository.TtsRepository
import com.example.mobile_dev_project.data.TtsState
import com.example.mobile_dev_project.ui.fake.TTsViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
 import javax.inject.Inject

@HiltViewModel
class TTsViewModel @Inject constructor(
    private val ttsRepository: TtsRepository,
    private val chapterRepository: ChapterRepository,
    private val contentRepository: ContentRepository
) : ViewModel(), TTsViewModelInterface {
    override val ttsState: StateFlow<TtsState> = ttsRepository.state
    private val _chapter = MutableStateFlow<UiChapter?>(null)
    val chapter: StateFlow<UiChapter?> = _chapter

    private val _content = MutableStateFlow<UiContent?>(null)
    val content: StateFlow<UiContent?> = _content

    fun setChapter(chapterId: Int) {
        viewModelScope.launch {
            val entity = chapterRepository.getChapterById(chapterId).firstOrNull()
            if (entity != null) {
                _chapter.value = UiChapter(
                    chapterId = entity.chapterId,
                    bookId = entity.bookId,
                    chapterOrder = entity.chapterOrder,
                    chapterTitle = entity.chapterName,
                    contentId = entity.contentId
                )
            }
        }
    }

    override fun prepareChapterById(chapterId: Int) {
        viewModelScope.launch {
            setChapter(chapterId)
            setContent()
            prepareTTs()
        }
    }

    fun setContent() {
        viewModelScope.launch {
            val ch = _chapter.value ?: return@launch
            val entity = contentRepository.getContentForChapter(ch.chapterId).firstOrNull()

            if (entity != null) {
                _content.value = UiContent(
                    contentId = entity.contentId,
                    chapterId = entity.chapterId,
                    content = entity.content
                )
            }
        }
    }

    fun prepareTTs() {
        viewModelScope.launch {
            val ch = _chapter.value
            val ct = _content.value

            if (ch != null && ct != null) {
                ttsRepository.prepare(
                    chapterId = ch.chapterId,
                    text = ct.content
                )
            }
        }
    }

    override fun playTTs() = ttsRepository.play()
    override fun pauseTTs() = ttsRepository.pause()
    override fun stopTTs() = ttsRepository.stop()
    override fun releaseTTs() = ttsRepository.release()
}
