package com.example.mobile_dev_project.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * viwemodel for the Search screen
 * - hold the search query text
 * -Expose computed matches from a static paragraph list, im using sample data that I created for milestone 1
 */
class SearchViewModel : ViewModel() {

    //entered query text
    var query by mutableStateOf("")
        private set

    //temp sample content so ui works
    //so later I can replace with backed current book content
    private val paragraphs = listOf(
        "Chapter 1 : THE BOY WHO LIVED",
        "Chapter 2 : THE VANISHING GLASS",
        "Chapter 3 : THE LETTERS FROM NO ONE",
        "Chapter 4 : THE KEEPER OF THE KEYS"
    )

    //Computed matches whenever quer changes
    // here resues the same function as in the screen to keep behavior aligned and testable
    val matches: List<Pair<Int, String>>
        get() = findMatches(paragraphs, query)

    //ui event when the user types in the search fiel
    fun onQueryChanged(newQuery: String) {
        query = newQuery
    }
}
