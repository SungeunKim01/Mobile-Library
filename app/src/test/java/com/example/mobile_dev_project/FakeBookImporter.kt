package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.importer.BookImporterContract
import com.example.mobile_dev_project.ui.model.ImportPhase
import com.example.mobile_dev_project.ui.model.ProgressState

class FakeBookImporter : BookImporterContract {
    var importCalledWith: List<Pair<String, String>>? = null
    var shouldFail: Boolean = false

    override suspend fun importBooks(
        sources: List<Pair<String, String>>,
        onProgress: (ProgressState) -> Unit
    ) {
        importCalledWith = sources
        onProgress(ProgressState(ImportPhase.DOWNLOADING, "Downloading..."))
        if (shouldFail) throw Exception("Fake download failed")
        onProgress(ProgressState(ImportPhase.DONE, "Done"))
    }
}
