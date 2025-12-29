package com.example.mobile_dev_project.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobile_dev_project.data.dao.BookDao
import com.example.mobile_dev_project.data.dao.ChapterDao
import com.example.mobile_dev_project.data.dao.ContentDao
import com.example.mobile_dev_project.data.entity.Book
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content

@Database(
    entities = [Book::class, Chapter::class, Content::class],
    version = 6,
    )
abstract class BookRoomDatabase : RoomDatabase(){

    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun contentDao(): ContentDao
    companion object {
        private var INSTANCE: BookRoomDatabase? = null

        fun getInstance(context: Context): BookRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookRoomDatabase::class.java,
                        "book_reading_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}