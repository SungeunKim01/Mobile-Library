package com.example.mobile_dev_project.ui.screens

import androidx.compose.material3.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_dev_project.R
import androidx.compose.runtime.getValue
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_dev_project.data.entity.Book
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap

//This will add everything together and add the title of the bookApp with the Logo on the top, in typography title size
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel(),
               onNavigateToDownload: () -> Unit = {}
) {
    val books by viewModel.allBooks.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
            .testTag("home_screen")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RestaurantLogo(modifier = Modifier.testTag("restaurant_logo"))

            Text(
                text = stringResource(R.string.title),
                //Had to force change the color because mine was not visible enough to be seen on page
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.testTag("title")
            )


        }
        DownloadBookButton(onNavigateToDownload = onNavigateToDownload)
        Bookshelf(books, viewModel)
    }
}
//This will display the Restaurant Logo on the top
@Composable
fun RestaurantLogo(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.mobile_library)
    Column {
        Image(
            painter = image,
            contentDescription = null,
            modifier = modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(1f),

            contentScale = ContentScale.Crop
        )
    }
}
//This Composable will display the books in Columns 1 by one once it is callled into Book Composable to get what each
// book will be formated adn say and then will be added to the column
@Composable
fun Bookshelf(books: List<Book>, viewModel: HomeScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bookshelf"),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        books.forEach { book ->
            Book(book, viewModel)
        }
    }
}
//This Composable is how each Book will be visuallized so in a row, on the left will be the book image, not finished until we add each book,
//On the Right is the last used text
@Composable
fun Book(book: Book, viewModel: HomeScreenViewModel){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().testTag("book")
    ) {
        BookCover(coverPath = book.bookCoverPath)
        Spacer(modifier = Modifier.width(16.dp))
        BookInfo(book = book, viewModel = viewModel)
    }
}
//This was actually hard as this needed to figure out how to load an image from db.
//Source used:https://developer.android.com/reference/android/graphics/Bitmap
//https://developer.android.com/reference/android/graphics/BitmapFactory
// Well what it does is that it gets the cover path and goes through the BitmapFactory and decodes the file
// Then it converts it to a ImageBitmap for the Composable, so it can now then display the stored image
@Composable
fun BookCover(coverPath: String?) {
    val bitmap = coverPath?.let{
        BitmapFactory.decodeFile(it)?.asImageBitmap()
    }
    if(bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }else {
        Image(
            painter = painterResource(R.drawable.mobile_library),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BookInfo(book: Book, viewModel: HomeScreenViewModel) {
    Column {
        Text(
            text = book.bookTitle ?: "No title this is bad!"
        )
        val displayDate = if (book.lastAccessed != null) {
            viewModel.formatDate(parseDateStringToLong(book.lastAccessed))
        } else {
            viewModel.formatDate(parseDateStringToLong(book.bookAdded))
        }
        Text(
            text = stringResource(R.string.last_use_text) + " ${displayDate}",
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
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



//This will be a button that will navigate to Download screen so that they can add a book
@Composable
fun DownloadBookButton(onNavigateToDownload: () -> Unit){
    Button(onClick = onNavigateToDownload,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("download_button"),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(stringResource(R.string.add_book))
    }
}
//Just added this so that i can preeview my code without doing so by starting the app.
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MobileDevProjectTheme {
        HomeScreen()
    }
}