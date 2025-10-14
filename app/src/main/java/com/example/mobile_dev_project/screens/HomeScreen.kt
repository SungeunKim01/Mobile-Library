package com.example.mobile_dev_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_dev_project.R
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme

//Temporary until we actually add books and dataclass
data class Book(val title: String, val coverId: Int, val lastAccess: String)
//List of books examples
val exampleBooks = listOf(
    Book("Book 1", 1, "Oct 14, 2025"),
    Book("Book 2", 2, "Oct 13, 2023"),
    Book("Book 3", 3, "Oct 12, 2024"),
    Book("Book 4", 4, "Oct 11, 2022")
)

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RestaurantLogo()

            Text(
                text = "Welcome to Mobile Library",
                style = MaterialTheme.typography.titleLarge
            )


        }
        Bookself(exampleBooks)
    }
}
    @Composable
    fun RestaurantLogo(modifier: Modifier = Modifier.Companion) {
        val image = painterResource(R.drawable.mobile_library)
        Column {
            Image(
                painter = image,
                contentDescription = null,
                modifier = modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(1f),

                contentScale = ContentScale.Companion.Crop
            )
        }
    }

@Composable
fun Bookself(books: List<Book>){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        books.forEach {
            book ->
            Book(book)
        }
    }
}

@Composable
fun Book(book: Book){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            //This is wrong I know, but i dont have images yet for the books
            painter = painterResource(R.drawable.mobile_library),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(1f),

            contentScale = ContentScale.Companion.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Last Used: ${book.lastAccess}")
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MobileDevProjectTheme {
        HomeScreen()
    }
}