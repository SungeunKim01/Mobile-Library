package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.TtsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A fake ViewModel for UI testing / previews.
 */
class FakeTtsViewModel : TTsViewModel(
    ttsRepository = null,
    chapterRepository = null,
    contentRepository = null
) {

    private val fakeState = MutableStateFlow<TtsState>(TtsState.Idle)
    override val ttsState: StateFlow<TtsState> = fakeState

    override fun playTTs() { fakeState.value = TtsState.Playing(1,0) }
    override fun pauseTTs() { fakeState.value = TtsState.Paused(1,0) }
    override fun stopTTs() { fakeState.value = TtsState.Stopped }
}

