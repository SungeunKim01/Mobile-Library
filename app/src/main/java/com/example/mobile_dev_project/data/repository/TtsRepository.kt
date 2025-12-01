package com.example.mobile_dev_project.data.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.mobile_dev_project.data.TtsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//This uses android text to speech not readium
// the inject is for hilt and the context is for
// the actual context needed
class TtsRepository @Inject constructor(
    private val context: Context
) {
    //This will bve the TTS engine needed to actualy do the tts
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    //This is for the UI to observe so meaning this is
    // what is needed to get the state
    private val _state = MutableStateFlow<TtsState>(TtsState.Idle)
    val state: StateFlow<TtsState> = _state

    //This is for the rate of the voice so fast or slow
    private val _rate = MutableStateFlow(1.0f)
    val rate: StateFlow<Float> = _rate

    //This is for the pitch meaning that is tis for the high and low voice
    private val _pitch = MutableStateFlow(1.0f)
    val pitch: StateFlow<Float> = _pitch


    //This is to track what is actually being read
    private var currentText: String = ""
    private var currentChapterId: Int = -1
    private var currentOffset: Int = 0

    //This will be the scope of the actual tts which will
    // know if anything changes so it will update the ui state
    private val scope = CoroutineScope(Dispatchers.Main)




}
