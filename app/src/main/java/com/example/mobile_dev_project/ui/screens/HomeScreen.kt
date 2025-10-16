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
                text = "Welcome to Mobile Library",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.testTag("title")
            )


        }
        DownloadBookButton(onNavigateToDownload = onNavigateToDownload)
        Bookself(viewModel.exampleBooks)
    }
}
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

@Composable
fun Bookself(books: List<Book>){
    Column(
        modifier = Modifier.fillMaxWidth()
            .testTag("bookself"),
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
        Text(text = "Last Used: ${book.lastAccess}")
    }
}
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
        Text("Add New Book")
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MobileDevProjectTheme {
        HomeScreen()
    }
}