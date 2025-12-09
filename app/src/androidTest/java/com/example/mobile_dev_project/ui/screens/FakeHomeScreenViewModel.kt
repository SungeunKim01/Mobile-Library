package com.example.mobile_dev_project.ui.screens


import com.example.mobile_dev_project.data.UiBook
import com.example.mobile_dev_project.data.entity.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeHomeScreenViewModel : HomeScreenViewModel(null) {

    private val _allBooks = MutableStateFlow<List<Book>>(generateMockBooks())
    override val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()

    //src: https://kotlinlang.org/docs/object-declarations.html#companion-objects
    private companion object {
        fun generateMockBooks(): List<Book> {
            return listOf(
                Book("Mock Book 1", "", "2025/01/01 12:00:00", "url1").apply { bookId = 1 },
                Book("Mock Book 2", "", "2025/01/02 12:00:00", "url2").apply { bookId = 2 },
                Book("Mock Book 3", "", "2025/01/03 12:00:00", "url3").apply { bookId = 3 },
                Book("Mock Book 4", "", "2025/01/04 12:00:00", "url4").apply { bookId = 4 }
            )
        }
    }

}
