package com.example.mobile_dev_project.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.mobile_dev_project.R

// provide the initial list of book source urls for the app, loaded from resources - strings.xml
@Singleton
class BookSourceRepository @Inject constructor(
    //request the Application context thru Hilt
    //@ApplicationContext -Hilt qualifier that guarantees i get the app level Context
    @ApplicationContext private val appContext: Context) {
    //return the list of initial book source urls from resources
    fun initialUrls(): List<String> =
        appContext.resources.getStringArray(R.array.book_urls).toList()
}