package com.example.mobile_dev_project.screens

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

//Temporary until we actually add books and dataclass
data class Book(val title: String, val coverId: Int, val lastAccess: String)
class HomeScreenViewModel : ViewModel() {

    //List of books examples
    val exampleBooks = mutableStateListOf(
        Book("Book 1", 1, "Oct 14, 2025"),
        Book("Book 2", 2, "Oct 13, 2023"),
        Book("Book 3", 3, "Oct 12, 2024"),
        Book("Book 4", 4, "Oct 11, 2022")
    )

    fun addBook(book: Book){
        exampleBooks.add(book)
    }
}


