package com.example.mobile_dev_project.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class HomeScreenViewModel @Inject constructor (
    private val repository: BookRepository
) : ViewModel() {
    private val _topThreeBooks = MutableStateFlow<List<Book>>(emptyList())
    val topThreeBooks: StateFlow<List<Book>> = _topThreeBooks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getBooksByLastAccessed().collect { books ->
                val sortedBooks = books.sortedByDescending { book ->
                    //This is actually fun which says if lastAccessed
                    // is null then go to bookAdded
                    book.lastAccessed ?: book.bookAdded
                }
                _topThreeBooks.value = sortedBooks.take(3)
            }
        }
    }
}


