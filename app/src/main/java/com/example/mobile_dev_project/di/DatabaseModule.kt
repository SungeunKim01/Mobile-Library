package com.example.mobile_dev_project.di

import android.content.Context
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.database.BookRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent:: class)
object DatabaseModule {

    // Provide the Room database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookRoomDatabase {
        return BookRoomDatabase.getInstance(context)
    }
    // Provide DAO (retrieved from database)
    @Provides
    fun provideBookDao(database: BookRoomDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideChapterDao(database: BookRoomDatabase): ChapterDao {
        return database.chapterDao()
    }

    @Provides
    fun provideContentDao(database: BookRoomDatabase): ContentDao {
        return database.contentDao()
    }
}