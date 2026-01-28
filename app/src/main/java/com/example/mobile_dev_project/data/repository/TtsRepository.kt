package com.example.mobile_dev_project.data.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.mobile_dev_project.data.TtsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import android.speech.tts.UtteranceProgressListener
//This uses android text to speech not readium
// the inject is for hilt and the context is for
// the actual context needed
class TtsRepository  constructor(
    private val context: Context,
    private val ttsEngine: TextToSpeech? = null
) {
    //This will bve the TTS engine needed to actualy do the tts
    private var tts: TextToSpeech? = ttsEngine
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

    //This will set up the tag and also the chunk size
    // meaning the speak texts in chunk and 1200 characters
    companion object {
        private const val TAG = "TtsRepository"
        private const val CHUNK_SIZE = 1200
    }

    //this will run when ttsRepo is created
    init {
        if (tts == null){
            initializeTts()
        }else {
            isInitialized = true
        }
    }

    private fun initializeTts() {
        tts = TextToSpeech(context) { status ->

            //This makes sures that if it status is good it will
            // make the engine set language to canadian english
            // set speechRate and Set pitch
            // elseit will jsut fail and give error
            if (status == TextToSpeech.SUCCESS) {
                tts?.let { engine ->
                    engine.setLanguage(Locale.CANADA)
                    engine.setSpeechRate(_rate.value)
                    engine.setPitch(_pitch.value)
                    isInitialized = true
                    Log.d(TAG, "TTS initialized successfully")
                }
            } else {

                Log.e(TAG, "TTS initialization failed")
                _state.value = TtsState.Error("TTS initialization failed")
            }
        }
    }

    //sets up the chapter for the playback
    suspend fun prepare(chapterId: Int?, text: String?) {
        scope.launch {
            _state.value = TtsState.Preparing
            currentText = text.toString()
            currentChapterId = chapterId!!
            currentOffset = 0
            _state.value = TtsState.Paused(chapterId, 0)
        }

    }

    fun play() {
        val current = _state.value

        //if paused it will resume at the saved position
        if (current is TtsState.Paused) {
            speakFromOffset(current.currentOffset)
            return
        }

        //So if stopped or idle it will just go back to the beginning
        if (current is TtsState.Stopped || current is TtsState.Idle) {
            speakFromOffset(0)
            return
        }
    }

    // this will pause the playback ans save the position
    fun pause() {
        tts?.stop()
        _state.value = TtsState.Paused(currentChapterId, currentOffset)
    }

    // this stopes the playback and resets to the beginning
    fun stop() {
        tts?.stop()
        currentOffset = 0
        _state.value = TtsState.Stopped
    }

    //This is the speakFromOffset helper this si the one where they
    //this makes it so the at text speaks form the given area, and then it will go to the next chunk
    private fun speakFromOffset(offset: Int) {
        //Gets position and updates the state to be playing
        currentOffset = offset
        _state.value = TtsState.Playing(currentChapterId, offset)

        //This will be used to calcuclate where the end fo the character is
        //And it grabs the chunk needed from the endOffset
        //then it sets a  unique id for this chunk
        val endOffset = minOf(offset + CHUNK_SIZE, currentText.length)
        val chunk = currentText.substring(offset, endOffset)
        val utteranceId = "tts_$offset"

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            //Must override it because it wants it not because i need it
            override fun onStart(utteranceId: String?) {}


            //This just checks when the chunk is finished so it just makes ssure if ther eis more text to read from or not
            //If there is none then it will just stop and reset to the beginning
            override fun onDone(utteranceId: String?) {
                if (endOffset < currentText.length) {
                    speakFromOffset(endOffset)
                } else {
                    scope.launch {
                        _state.value = TtsState.Stopped
                        currentOffset = 0
                    }
                }
            }

            override fun onError(utteranceId: String?) {
                scope.launch {
                    _state.value = TtsState.Error("Error")
                }
            }
        })
        tts?.speak(chunk, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }
    //This is for when you are done with the tts so it just removes it and shuts it down
    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}