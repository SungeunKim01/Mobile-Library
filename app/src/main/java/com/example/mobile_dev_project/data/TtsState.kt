package com.example.mobile_dev_project.data

sealed class TtsState {

    //Tts is not prepared
    object Idle : TtsState()

    //Tts is starting to turn on meaning its preparing the text
    object Preparing : TtsState()

    //Tts has been stopped
    object Stopped : TtsState()

    //This is for the playing meaning it is currently active meaning its reading
    data class Playing(
        val chapterId: Int,
        val currentOffset: Int = 0
    ) : TtsState()
    //Tts is paused, can resume
    data class Paused(
        val chapterId: Int,
        val currentOffset: Int = 0
    ) : TtsState()

    data class Error(val message: String) : TtsState()

}