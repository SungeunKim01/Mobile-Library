package com.example.mobile_dev_project.data.importer

import com.example.mobile_dev_project.ui.model.ProgressState

interface BookImporterContract {
    suspend fun importBooks(
        sources: List<Pair<String, String>>,
        onProgress: (ProgressState) -> Unit
    )
}
