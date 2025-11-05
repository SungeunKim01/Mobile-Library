package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.importer.BookImporter
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.data.repository.BookSourceRepository
import com.example.mobile_dev_project.ui.model.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val importer: BookImporter,
    private val sourceRepo: BookSourceRepository,
    private val books: BookRepository
) : ViewModel() {

    //bookshelf flow so ui auto updates
    val shelf = books.allBooks

    // call this once when Home starts
    fun importInitialBooks(onProgress: (ProgressState) -> Unit) {
        viewModelScope.launch {
            // strings.xml, so like [url1, url2, url3]
            val urls = sourceRepo.initialUrls()
            // make (label,url) pairs
            // label = lastPathSegment w/o .zip
            val sources: List<Pair<String,String>> = urls.map { url ->
                val label = url.toUri().lastPathSegment?.removeSuffix(".zip") ?: "Book"
                label to url
            }
            importer.importBooks(sources, onProgress)
        }
    }
}
