package com.example.mobile_dev_project.ui.model

// 4 phases need to show
enum class ImportPhase { DOWNLOADING, UNZIPPING, PARSING, POPULATING, DONE}

// state for the screen
data class ProgressState(
    val phase: ImportPhase,
    val message: String,
    val detail: String? = null,
    val done: String? = null
)
