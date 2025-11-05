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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_dev_project.R
import com.example.mobile_dev_project.ui.theme.MobileDevProjectTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

//This will add everything together and add the title of the bookApp with the Logo on the top, in typography title size
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel(),
               onNavigateToDownload: () -> Unit = {}
) {
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
        Bookself(viewModel.exampleBooks)
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
fun Bookself(books: List<Book>){
    Column(
        modifier = Modifier.fillMaxWidth()
            .testTag("bookshelf"),
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        books.forEach {
            book ->
            Book(book)
        }
    }
}
//This Composable is how each Book will be visuallized so in a row, on the left will be the book image, not finished until we add each book,
//On the Right is the last used text
@Composable
fun Book(book: Book){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().testTag("book")
    ) {
        Image(
            //This is wrong I know, but i dont have images yet for the books
            painter = painterResource(R.drawable.mobile_library),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(1f),

            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(R.string.last_use_text) + " ${book.lastAccess}",
            color = MaterialTheme.colorScheme.onSecondary)
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