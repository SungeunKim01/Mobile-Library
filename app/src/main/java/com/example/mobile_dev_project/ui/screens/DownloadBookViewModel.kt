package com.example.mobile_dev_project.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.importer.BookImporter
import com.example.mobile_dev_project.ui.model.ImportPhase
import com.example.mobile_dev_project.ui.model.ProgressState
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.mobile_dev_project.data.importer.BookImporterContract

/**
 * this is ViewModel fo download book screen
 * -Hold the url text the user enter
 * -trigger the import pipeline thru BookImporter (download then unzip then parse then persist)
 * - also forwards progress msg (Downloading/Unzipping/Parsing/Populating/DONE)
 *
 * here I inject BookImporter -Hilt. Repositories/DAOs are used inside the importer
 */
@HiltViewModel
class DownloadBookViewModel @Inject constructor(
    private val importer: BookImporterContract,
    private val repo: BookRepository?)
    : ViewModel() {

    // TextField state
    var url by mutableStateOf("")
        private set

    // btn enabled when non blank
    val canAdd: Boolean get() = url.isNotBlank() && !isImporting

    //ui feedback
    var isImporting by mutableStateOf(false)
        private set

    var errorText by mutableStateOf<String?>(null)
        private set

    // latest progress
    var progress by mutableStateOf<ProgressState?>(null)
        private set

    //Called by the TextField on each user edit
    fun onUrlChanged(newUrl: String) {
        url = newUrl
        // clear any previous error while typing
        errorText = null
    }

    //validation as per class labs
    private fun looksLikeHttpUrl(s: String): Boolean =
        s.startsWith("http://") || s.startsWith("https://")

    //create stable folder id for /files/books/{id}
    private fun newUserBookId(): String =
        (System.currentTimeMillis() / 1000).toString()


    /**
     * User tapped "Add to Bookshelf"
     * -validate url
     * -generate a folder id
     * - call repository.importBooks(listOf(id to url)) & forward progress to ui
     */
    fun onAddClicked() {
        val trimmed = url.trim()
        if (!looksLikeHttpUrl(trimmed)) {
            errorText = "Please enter a valid URL (http/https)"
            return
        }

        viewModelScope.launch {
            isImporting = true
            progress = ProgressState(ImportPhase.DOWNLOADING, "Starting download...", detail = trimmed)

            try {
                val id = newUserBookId()
                // reuse repository pipeline exactly as is
                importer.importBooks(listOf(id to trimmed)) { step ->
                    // forward progress to ui
                    progress = step
                }
                // success -clear the field to signal completion
                url = ""
            } catch (e: Exception) {
                errorText = "Failed: ${e.message ?: "error"}"
            } finally {
                isImporting = false
            }
        }
    }
}
