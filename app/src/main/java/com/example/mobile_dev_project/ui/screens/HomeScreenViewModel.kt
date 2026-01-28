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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@HiltViewModel
open class HomeScreenViewModel @Inject constructor (
    private val repository: BookRepository?
) : ViewModel() {
    private val _allBooks = MutableStateFlow<List<Book>>(emptyList())
    open val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()
    init {
        viewModelScope.launch {
            repository?.getBooksByLastAccessed()?.collect { books ->
                val sortedBooks = books.sortedByDescending { book ->
                    book.lastAccessed ?: book.bookAdded
                }
                _allBooks.value = sortedBooks
            }
        }
    }

    //Function for Date that will format the date the way I want
    fun formatDate(timestamp: Long?): String {
        if (timestamp == null) {
            return "Nothing"
        }
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun parseDateStringToLong(str: String?): Long? {
        if (str.isNullOrBlank()) return null
        return try {
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            sdf.parse(str)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun updateLastAccessed(bookId: Int) {
        viewModelScope.launch {
            val timestamp = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                .format(Date())

            repository?.updateLastAccessed(bookId, timestamp)
        }
    }

}


