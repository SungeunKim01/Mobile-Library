package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.TtsState
import com.example.mobile_dev_project.ui.fake.TTsViewModelInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A fake ViewModel for UI testing / previews.
 */
class FakeTTsViewModel : TTsViewModelInterface {

    private val _ttsState = MutableStateFlow<TtsState>(TtsState.Idle)
    override val ttsState: StateFlow<TtsState> = _ttsState

    private var lastChapterId: Int? = null

    override fun prepareChapterById(chapterId: Int) {
        lastChapterId = chapterId
        _ttsState.value = TtsState.Paused(chapterId, 0)
    }

    override fun playTTs() {
        val chapter = lastChapterId ?: -1
        val offset = (ttsState.value as? TtsState.Paused)?.currentOffset ?: 0
        _ttsState.value = TtsState.Playing(chapter, offset)
    }

    override fun pauseTTs() {
        val chapter = lastChapterId ?: -1
        val offset = (ttsState.value as? TtsState.Playing)?.currentOffset ?: 0
        _ttsState.value = TtsState.Paused(chapter, offset)
    }

    override fun stopTTs() {
        _ttsState.value = TtsState.Stopped
    }

    override fun releaseTTs() {
        _ttsState.value = TtsState.Idle
    }
}
