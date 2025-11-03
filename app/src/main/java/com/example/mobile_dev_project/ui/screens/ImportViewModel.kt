package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.importer.BookImporter
import com.example.mobile_dev_project.data.repository.BookRepository
import com.example.mobile_dev_project.ui.model.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val importer: BookImporter,
    private val books: BookRepository
) : ViewModel() {
    val shelf = books.allBooks

    fun startImport(sources: List<Pair<String,String>>, onProgress: (ProgressState)->Unit) {
        viewModelScope.launch {
            importer.importBooks(sources, onProgress)
        }
    }

}
