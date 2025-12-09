package com.example.mobile_dev_project.ui.fake


import com.example.mobile_dev_project.data.TtsState
import kotlinx.coroutines.flow.StateFlow

/**
 * This interface will only be important so that faking the TTsViewModel doesn't
 * interfere with actual tts stuff.
 */
interface TTsViewModelInterface {
    val ttsState: StateFlow<TtsState>
    fun prepareChapterById(chapterId: Int)
    fun playTTs()
    fun pauseTTs()
    fun stopTTs()
    fun releaseTTs()
}