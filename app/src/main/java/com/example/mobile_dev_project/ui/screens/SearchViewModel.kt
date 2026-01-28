package com.example.mobile_dev_project.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.SearchResult
import com.example.mobile_dev_project.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * viwemodel for the Search screen
 * - hold the current search query text
 * - expose the current list of search results
 * -show loading and err state
 * - call searchRepository on viewModelScope
 */
@HiltViewModel
class SearchViewModel @Inject constructor (
    private val searchRepository: SearchRepository) : ViewModel() {

    // text entered by user in the search field
    var query by mutableStateOf("")
        private set

    // current search results from the db
    var results by mutableStateOf<List<SearchResult>>(emptyList())
        private set

    // flags for ui feedback
    var isSearching by mutableStateOf(false)
        private set

    var errMsg by mutableStateOf<String?>(null)
        private set

    //this called from the ui whenever the user types in the search field
    //so update the query state and trigger new search
    fun onQueryChanged(newQuery: String) {
        query = newQuery
        performSearch()
    }

    //Run the search in coroutin
    private fun performSearch() {
        val trimmed = query.trim()

        // clear the results if the query is blank otherwise do nothing
        if (trimmed.isBlank()) {
            results = emptyList()
            errMsg = null
            return
        }

        viewModelScope.launch {
            isSearching = true
            errMsg = null

            try {
                // ask repo to search db
                val hits = searchRepository.search(trimmed)
                results = hits
            } catch (e: Exception) {
                results = emptyList()
                errMsg = e.message ?: "Search failed"
            } finally {
                isSearching = false
            }
        }
    }
}